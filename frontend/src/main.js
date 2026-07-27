import './style.css';

const storageKey = 'scriptwriter-session';
let session = JSON.parse(localStorage.getItem(storageKey) || 'null');
let users = [];
let categories = [];

const icon = (name) => ({
  pen: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="m14.7 4.3 5 5M4 20l3.6-.7L19.7 7.2a2.1 2.1 0 0 0-3-3L4.6 16.3 4 20Z"/></svg>',
  users: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M16 20v-1.5a4.5 4.5 0 0 0-4.5-4.5h-4A4.5 4.5 0 0 0 3 18.5V20M9.5 10.5A3.5 3.5 0 1 0 9.5 3a3.5 3.5 0 0 0 0 7.5ZM17 11a3 3 0 1 0 0-6M21 20v-1.5a4.5 4.5 0 0 0-3-4.3"/></svg>',
  tags: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M20 13 13 20 3 10V4h6l11 9Z M7.5 7.5h.01"/></svg>',
  arrow: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M5 12h14M13 6l6 6-6 6"/></svg>',
  logout: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M10 17l5-5-5-5M15 12H3M13 4h5a3 3 0 0 1 3 3v10a3 3 0 0 1-3 3h-5"/></svg>',
}[name]);

async function api(path, options = {}, retry = true) {
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  if (session?.accessToken) headers.Authorization = `Bearer ${session.accessToken}`;
  let response = await fetch(path, { ...options, headers });

  if (response.status === 401 && session?.refreshToken && retry) {
    const renewed = await fetch('/user/refresh', {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ refreshToken: session.refreshToken }),
    });
    if (renewed.ok) {
      const tokens = await renewed.json();
      session.accessToken = tokens.accessToken;
      persistSession();
      return api(path, options, false);
    }
    clearSession();
    render();
    throw new Error('Your session ended. Please sign in again.');
  }

  if (!response.ok) {
    const body = await response.json().catch(() => null);
    throw new Error(body?.message || (typeof body === 'string' ? body : `Request failed (${response.status})`));
  }
  if (response.status === 204) return null;
  return response.json();
}

function persistSession() { localStorage.setItem(storageKey, JSON.stringify(session)); }
function clearSession() { localStorage.removeItem(storageKey); session = null; users = []; categories = []; }
function escapeHtml(value = '') { return String(value).replace(/[&<>'"]/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#039;', '"': '&quot;' })[c]); }
function initials(name = '') { return name.split(/\s+/).map(word => word[0]).join('').slice(0, 2).toUpperCase() || 'SW'; }
function toast(message, type = 'success') {
  const item = document.createElement('div'); item.className = `toast ${type}`; item.textContent = message;
  document.body.append(item); setTimeout(() => item.remove(), 3800);
}

function authView(mode = 'login') {
  const isLogin = mode === 'login';
  document.querySelector('#app').innerHTML = `
    <main class="auth-shell">
      <section class="auth-aside">
        <a class="brand brand-light" href="#">${icon('pen')} <span>scriptwriter</span></a>
        <div class="aside-copy">
          <p class="eyebrow">A clear workspace</p>
          <h1>Good stories begin with a sharper setup.</h1>
          <p>Keep the people and permissions behind your creative process organized, so the work can stay in focus.</p>
        </div>
        <div class="script-mark" aria-hidden="true"><i></i><i></i><i></i><i></i><i></i></div>
        <p class="aside-footer">Built for the first draft — and everything around it.</p>
      </section>
      <section class="auth-panel">
        <div class="auth-card">
          <span class="kicker">${isLogin ? 'Welcome back' : 'Start your workspace'}</span>
          <h2>${isLogin ? 'Sign in to continue.' : 'Create your account.'}</h2>
          <p class="muted">${isLogin ? 'Use your Scriptwriter email and password.' : 'We’ll send a verification code to finish setup.'}</p>
          <form id="auth-form" class="form-stack">
            ${!isLogin ? '<label>Display name<input required name="name" autocomplete="name" placeholder="e.g. Ava Martinez" /></label><label>Email address<input required type="email" name="email" autocomplete="email" placeholder="you@example.com" /></label>' : '<label>Email address<input required type="email" name="email" autocomplete="email" placeholder="you@example.com" /></label>'}
            <label>Password<input required type="password" name="password" autocomplete="${isLogin ? 'current-password' : 'new-password'}" minlength="8" placeholder="At least 8 characters" /></label>
            <button class="button primary" type="submit">${isLogin ? 'Sign in' : 'Create account'} ${icon('arrow')}</button>
          </form>
          <p class="switcher">${isLogin ? 'New to Scriptwriter?' : 'Already have an account?'} <button id="switch-auth">${isLogin ? 'Create one' : 'Sign in'}</button></p>
        </div>
      </section>
    </main>`;
  document.querySelector('#switch-auth').onclick = () => authView(isLogin ? 'register' : 'login');
  document.querySelector('#auth-form').onsubmit = event => submitAuth(event, isLogin);
}

async function submitAuth(event, isLogin) {
  event.preventDefault();
  const button = event.currentTarget.querySelector('button');
  const data = Object.fromEntries(new FormData(event.currentTarget));
  button.disabled = true; button.textContent = isLogin ? 'Signing in…' : 'Creating account…';
  try {
    if (isLogin) {
      const response = await api('/user/login', { method: 'POST', body: JSON.stringify(data) });
      if (response.code === 'EMAIL_VERIFICATION_REQUIRED') {
        verificationView(response.email);
        return;
      }
      session = response;
      persistSession(); render();
    } else {
      await api('/user/register', { method: 'POST', body: JSON.stringify(data) });
      verificationView(data.email);
    }
  } catch (error) { toast(error.message, 'error'); button.disabled = false; button.innerHTML = `${isLogin ? 'Sign in' : 'Create account'} ${icon('arrow')}`; }
}

function verificationView(email) {
  document.querySelector('#app').innerHTML = `<main class="auth-shell"><section class="auth-aside compact"><a class="brand brand-light" href="#">${icon('pen')} <span>scriptwriter</span></a><div class="aside-copy"><p class="eyebrow">One last thing</p><h1>Check your inbox.</h1><p>We sent a one-time code to verify your account.</p></div></section><section class="auth-panel"><div class="auth-card"><span class="kicker">Verify email</span><h2>Enter your code.</h2><p class="muted">Sent to <strong>${escapeHtml(email)}</strong></p><form id="verify-form" class="form-stack"><label>Verification code<input required name="code" inputmode="numeric" autocomplete="one-time-code" placeholder="6-digit code" /></label><button class="button primary" type="submit">Verify account ${icon('arrow')}</button></form><p class="switcher"><button id="back-login">Back to sign in</button></p></div></section></main>`;
  document.querySelector('#back-login').onclick = () => authView();
  document.querySelector('#verify-form').onsubmit = async event => {
    event.preventDefault(); const button = event.currentTarget.querySelector('button'); button.disabled = true;
    try { session = await api('/user/verify', { method: 'POST', body: JSON.stringify({ email, code: new FormData(event.currentTarget).get('code') }) }); persistSession(); render(); }
    catch (error) { toast(error.message, 'error'); button.disabled = false; }
  };
}

function dashboard() {
  const user = users[0];
  document.querySelector('#app').innerHTML = `
    <main class="workspace">
      <aside class="sidebar">
        <a class="brand" href="#">${icon('pen')} <span>scriptwriter</span></a>
        <div class="workspace-title"><span class="dot"></span>Workspace</div>
        <nav><button class="nav-item active" data-page="overview">${icon('pen')} Overview</button><button class="nav-item" data-page="people">${icon('users')} People <span>${users.length}</span></button><button class="nav-item" data-page="categories">${icon('tags')} Categories <span>${categories.length}</span></button></nav>
        <div class="sidebar-bottom"><p>Script tools are on the way.</p><button id="logout" class="nav-item logout">${icon('logout')} Sign out</button></div>
      </aside>
      <section class="content"><header><div><p class="eyebrow">Your workspace</p><h1 id="page-title">Overview</h1></div><div class="profile-chip"><span class="avatar">${initials(user?.name)}</span><span>${escapeHtml(user?.name || 'Member')}</span></div></header><div id="page-content"></div></section>
    </main>`;
  document.querySelectorAll('[data-page]').forEach(button => button.onclick = () => setPage(button.dataset.page));
  document.querySelector('#logout').onclick = logout;
  setPage('overview');
}

function setPage(page) {
  document.querySelectorAll('.nav-item[data-page]').forEach(button => button.classList.toggle('active', button.dataset.page === page));
  const title = { overview: 'Overview', people: 'People', categories: 'Categories' }[page];
  document.querySelector('#page-title').textContent = title;
  const target = document.querySelector('#page-content');
  if (page === 'overview') target.innerHTML = overviewHtml();
  if (page === 'people') target.innerHTML = peopleHtml();
  if (page === 'categories') target.innerHTML = categoriesHtml();
  bindPage(page);
}

function overviewHtml() { return `<section class="hero"><p class="eyebrow">Control room</p><h2>Make room for the work<br/><em>only you can do.</em></h2><p>This workspace is connected to your Scriptwriter API. Manage members and access groups while script endpoints are being built.</p><button class="button ink" data-go="people">Manage people ${icon('arrow')}</button><div class="hero-lines" aria-hidden="true"><i></i><i></i><i></i><i></i></div></section><section class="stats"><article><span>People</span><strong>${users.length}</strong><p>Registered workspace accounts</p></article><article><span>Access groups</span><strong>${categories.length}</strong><p>Categories available to assign</p></article><article><span>Scripts</span><strong>—</strong><p>API endpoints not available yet</p></article></section><section class="notice"><span>${icon('pen')}</span><div><h3>Script management is coming next</h3><p>The backend currently has no script or profile endpoints, so this frontend stays intentionally focused on the API features that are ready today.</p></div></section>`; }
function peopleHtml() { return `<section class="section-heading"><div><h2>Workspace people</h2><p>Every account registered through the API.</p></div><button class="button quiet" id="refresh-people">Refresh list</button></section><section class="table-card"><div class="table-head"><span>Person</span><span>Email</span><span>Category ID</span><span></span></div>${users.length ? users.map(person => `<div class="table-row"><div class="person"><span class="avatar warm">${initials(person.name)}</span><strong>${escapeHtml(person.name)}</strong></div><span>${escapeHtml(person.email)}</span><span class="id-pill">${person.userCategoryId ?? '—'}</span><button class="text-button promote" data-id="${person.id}">Make admin</button></div>`).join('') : `<div class="empty"><h3>No people found</h3><p>Register an account to see it here.</p></div>`}</section>`; }
function categoriesHtml() { return `<section class="section-heading"><div><h2>Access groups</h2><p>Create and maintain the categories that organize members.</p></div></section><section class="category-layout"><form id="category-form" class="new-category"><span class="label">New category</span><h3>Add a group</h3><label>Name<input required name="name" placeholder="e.g. Editor" /></label><button class="button primary" type="submit">Create category ${icon('arrow')}</button></form><section class="category-list">${categories.length ? categories.map(category => `<article class="category-card"><span class="tag-icon">${icon('tags')}</span><div><h3>${escapeHtml(category.name)}</h3><p>Category #${category.id}</p></div><button class="text-button delete-category" data-name="${encodeURIComponent(category.name)}">Remove</button></article>`).join('') : `<div class="empty"><h3>No categories yet</h3><p>Create your first access group.</p></div>`}</section></section>`; }

function bindPage(page) {
  document.querySelectorAll('[data-go]').forEach(button => button.onclick = () => setPage(button.dataset.go));
  if (page === 'people') {
    document.querySelector('#refresh-people').onclick = () => loadData();
    document.querySelectorAll('.promote').forEach(button => button.onclick = () => promote(button.dataset.id));
  }
  if (page === 'categories') {
    document.querySelector('#category-form').onsubmit = createCategory;
    document.querySelectorAll('.delete-category').forEach(button => button.onclick = () => deleteCategory(decodeURIComponent(button.dataset.name)));
  }
}

async function loadData() {
  try { [users, categories] = await Promise.all([api('/user/user'), api('/user/category')]); dashboard(); }
  catch (error) { toast(error.message, 'error'); }
}
async function promote(id) { try { await api(`/admin/users/${id}/promote`, { method: 'PATCH' }); toast('Member promoted to admin.'); await loadData(); } catch (error) { toast(error.message, 'error'); } }
async function createCategory(event) { event.preventDefault(); const name = new FormData(event.currentTarget).get('name'); try { await api('/user/category', { method: 'POST', body: JSON.stringify({ name }) }); toast('Category created.'); await loadData(); setPage('categories'); } catch (error) { toast(error.message, 'error'); } }
async function deleteCategory(name) { if (!confirm(`Remove the ${name} category?`)) return; try { await api('/user/category', { method: 'DELETE', body: JSON.stringify({ name }) }); toast('Category removed.'); await loadData(); setPage('categories'); } catch (error) { toast(error.message, 'error'); } }
async function logout() { try { if (session?.refreshToken) await api('/user/logout', { method: 'POST', body: JSON.stringify({ refreshToken: session.refreshToken }) }); } catch { /* local sign-out still succeeds */ } clearSession(); authView(); }
function render() { session ? loadData() : authView(); }
render();
