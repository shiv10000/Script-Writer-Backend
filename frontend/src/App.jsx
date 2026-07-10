import { useRef, useState } from 'react'
import {
  AnimatePresence,
  motion,
  useReducedMotion,
  useScroll,
  useTransform,
} from 'framer-motion'
import {
  ArrowRight,
  CalendarDays,
  Check,
  ChevronRight,
  Clock3,
  Film,
  FolderOpen,
  Instagram,
  Menu,
  Play,
  Sparkles,
  UserRound,
  X,
} from 'lucide-react'

const ease = [0.22, 1, 0.36, 1]

function Mark({ light = false }) {
  return (
    <a className={`brand ${light ? 'brand--light' : ''}`} href="#top" aria-label="Script Writer home">
      <span className="brand-mark" aria-hidden="true">
        <i />
        <i />
        <i />
      </span>
      <span>Script Writer</span>
    </a>
  )
}

function Reveal({ children, className = '', delay = 0, y = 28 }) {
  const reduceMotion = useReducedMotion()
  return (
    <motion.div
      className={className}
      initial={reduceMotion ? false : { opacity: 0, y }}
      whileInView={reduceMotion ? undefined : { opacity: 1, y: 0 }}
      viewport={{ once: true, amount: 0.22 }}
      transition={{ duration: 0.75, delay, ease }}
    >
      {children}
    </motion.div>
  )
}

function Header() {
  const [open, setOpen] = useState(false)
  return (
    <header className="site-header">
      <div className="header-inner">
        <Mark />
        <nav className="desktop-nav" aria-label="Primary navigation">
          <a href="#features">Features</a>
          <a href="#workflow">How it works</a>
          <a href="#creators">For creators</a>
        </nav>
        <div className="header-actions">
          <a className="login-link" href="#signin">Sign in</a>
          <a className="button button--dark button--small" href="#start">Start writing <ArrowRight size={16} /></a>
        </div>
        <button className="menu-button" onClick={() => setOpen(true)} aria-label="Open menu"><Menu /></button>
      </div>
      <AnimatePresence>
        {open && (
          <motion.div className="mobile-menu" initial={{ opacity: 0, y: -16 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -16 }}>
            <div className="mobile-menu-top"><Mark /><button onClick={() => setOpen(false)} aria-label="Close menu"><X /></button></div>
            <nav aria-label="Mobile navigation">
              <a href="#features" onClick={() => setOpen(false)}>Features</a>
              <a href="#workflow" onClick={() => setOpen(false)}>How it works</a>
              <a href="#creators" onClick={() => setOpen(false)}>For creators</a>
              <a href="#signin" onClick={() => setOpen(false)}>Sign in</a>
            </nav>
            <a className="button button--red" href="#start" onClick={() => setOpen(false)}>Start writing <ArrowRight size={17} /></a>
          </motion.div>
        )}
      </AnimatePresence>
    </header>
  )
}

function ScriptEditor() {
  return (
    <div className="editor-shell">
      <aside className="editor-sidebar">
        <div className="traffic-lights"><i /><i /><i /></div>
        <p className="sidebar-label">Workspace</p>
        <div className="sidebar-item active"><FolderOpen size={15} /> All scripts <span>12</span></div>
        <div className="sidebar-item"><Film size={15} /> Ready to shoot <span>4</span></div>
        <p className="sidebar-label category-label">Categories</p>
        <div className="sidebar-item"><i className="dot dot--red" /> YouTube</div>
        <div className="sidebar-item"><i className="dot dot--blue" /> Reels</div>
        <div className="sidebar-item"><i className="dot dot--yellow" /> Tutorials</div>
      </aside>
      <div className="editor-main">
        <div className="editor-topbar">
          <span className="crumb">Scripts <ChevronRight size={13} /> YouTube</span>
          <span className="saved"><Check size={13} /> Saved</span>
        </div>
        <div className="editor-page">
          <div className="editor-meta"><span>YOUTUBE</span><span>8 MIN READ</span></div>
          <h3>Why your best ideas arrive too late</h3>
          <div className="script-block">
            <b>HOOK</b>
            <p>Ever had the perfect video idea—three hours after you needed it?</p>
          </div>
          <div className="script-lines" aria-hidden="true">
            <i className="line line--long" /><i className="line" /><i className="line line--mid" />
            <i className="line line--long" /><i className="line line--short" />
          </div>
          <div className="shoot-chip"><CalendarDays size={15} /><span><small>SHOOT DAY</small>Friday, 18 July</span></div>
        </div>
      </div>
      <motion.div
        className="floating-note"
        initial={{ opacity: 0, x: 30, rotate: 4 }}
        animate={{ opacity: 1, x: 0, rotate: -3 }}
        transition={{ delay: 1.15, duration: 0.8, ease }}
      >
        <span>NEW IDEA</span>
        Start with the mistake every creator makes...
      </motion.div>
    </div>
  )
}

function Hero() {
  const ref = useRef(null)
  const reduceMotion = useReducedMotion()
  const { scrollYProgress } = useScroll({ target: ref, offset: ['start start', 'end start'] })
  const editorY = useTransform(scrollYProgress, [0, 1], [0, reduceMotion ? 0 : 90])
  const editorRotate = useTransform(scrollYProgress, [0, 1], [0, reduceMotion ? 0 : -1.5])

  return (
    <section className="hero" id="top" ref={ref}>
      <div className="hero-copy page-width">
        <motion.p className="eyebrow" initial={{ opacity: 0, y: 12 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.15 }}>YOUR SCRIPT WORKSPACE</motion.p>
        <motion.h1 initial={{ opacity: 0, y: 40 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.9, ease }}>
          From idea to <em>shoot day.</em>
        </motion.h1>
        <motion.p className="hero-subtitle" initial={{ opacity: 0, y: 24 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.25, duration: 0.8, ease }}>
          Organize every video idea, script, and shoot day in one workspace built for creators.
        </motion.p>
        <motion.div className="hero-actions" initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.4, duration: 0.7 }}>
          <a className="button button--red" href="#start">Start writing <ArrowRight size={18} /></a>
          <a className="text-link" href="#workflow"><span><Play size={13} fill="currentColor" /></span> See it in action</a>
        </motion.div>
        <motion.p className="hero-note" initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ delay: 0.65 }}>FOR YOUTUBE · REELS · TUTORIALS · AND MORE</motion.p>
      </div>
      <motion.div className="hero-editor-wrap page-width" style={{ y: editorY, rotate: editorRotate }}>
        <ScriptEditor />
      </motion.div>
      <div className="hero-rule" />
    </section>
  )
}

const tickerItems = ['YouTube', 'Reels', 'Tutorials', 'Shorts', 'Campaigns', 'Podcasts', 'Brand stories', 'Video essays']

function Ticker() {
  return (
    <section className="ticker" aria-label="Content types">
      <div className="ticker-track">
        {[...tickerItems, ...tickerItems].map((item, index) => <span key={`${item}-${index}`}><Sparkles size={14} /> {item}</span>)}
      </div>
    </section>
  )
}

function JourneyCard() {
  return (
    <div className="journey-card">
      <div className="journey-card-top"><span>NEW SCRIPT</span><span>•••</span></div>
      <h4>Why your best ideas arrive too late</h4>
      <p>Open with the moment the idea disappears...</p>
      <div className="journey-tags"><span>YOUTUBE</span><span><Clock3 size={11} /> 8 min</span></div>
    </div>
  )
}

function ScriptJourney() {
  const section = useRef(null)
  const reduceMotion = useReducedMotion()
  const { scrollYProgress } = useScroll({ target: section, offset: ['start start', 'end end'] })
  const x = useTransform(scrollYProgress, [0, 0.32, 0.66, 1], ['-24vw', '-4vw', '17vw', '17vw'])
  const y = useTransform(scrollYProgress, [0, 0.32, 0.66, 1], [-100, -15, -15, 125])
  const rotate = useTransform(scrollYProgress, [0, 0.3, 0.65, 1], [-7, -2, 3, 0])
  const progress = useTransform(scrollYProgress, [0.04, 0.95], ['0%', '100%'])

  return (
    <section className="journey" id="workflow" ref={section}>
      <div className="journey-sticky">
        <div className="journey-heading page-width">
          <p className="eyebrow eyebrow--light">FROM FIRST THOUGHT TO RECORD</p>
          <h2>One clear path from <em>idea to camera.</em></h2>
          <p>Capture the thought, organize the script, and know exactly what you are shooting next.</p>
        </div>
        <div className="journey-scene page-width">
          <div className="journey-progress"><motion.i style={{ height: reduceMotion ? '100%' : progress }} /></div>
          <div className="journey-destination journey-destination--idea">
            <span>1</span><div><small>CAPTURE</small><b>Fresh idea</b></div>
          </div>
          <div className="journey-destination journey-destination--category">
            <span>2</span><div><small>ORGANIZE</small><b>YouTube</b></div>
          </div>
          <div className="journey-destination journey-destination--calendar">
            <span>3</span><div><small>SCHEDULE</small><b>18 JUL</b></div>
          </div>
          <motion.div className="moving-card" style={reduceMotion ? undefined : { x, y, rotate }}><JourneyCard /></motion.div>
        </div>
      </div>
    </section>
  )
}

function LibraryVisual() {
  const cards = [
    ['YouTube', 'Why your best ideas arrive too late', '18 Jul', 'red'],
    ['Reels', 'The two-second hook test', '22 Jul', 'blue'],
    ['Tutorial', 'A calmer editing workflow', 'Draft', 'yellow'],
  ]
  return (
    <div className="library-visual">
      <div className="visual-toolbar"><span><FolderOpen size={15} /> All scripts</span><button>+ New script</button></div>
      <div className="library-grid">
        {cards.map(([type, title, date, color], index) => (
          <motion.div className="library-card" key={title} whileHover={{ y: -8, rotate: index - 1 }} transition={{ type: 'spring', stiffness: 300 }}>
            <i className={`card-stripe card-stripe--${color}`} /><small>{type}</small><h4>{title}</h4><span><CalendarDays size={13} /> {date}</span>
          </motion.div>
        ))}
      </div>
    </div>
  )
}

function CalendarVisual() {
  return (
    <div className="calendar-visual">
      <div className="calendar-head"><div><small>SHOOT PLAN</small><b>July 2026</b></div><span>‹ &nbsp; ›</span></div>
      <div className="week-row">{['MON 13', 'TUE 14', 'WED 15', 'THU 16', 'FRI 17', 'SAT 18', 'SUN 19'].map(day => <span key={day}>{day}</span>)}</div>
      <div className="calendar-grid">
        {Array.from({ length: 7 }).map((_, i) => <div className={i === 5 ? 'shoot-day' : ''} key={i}>{i === 5 && <><small>SHOOT DAY</small><b>3 scripts</b><i><Film size={14} /></i></>}</div>)}
      </div>
      <motion.div className="calendar-script-pill" animate={{ y: [0, -6, 0] }} transition={{ repeat: Infinity, duration: 3, ease: 'easeInOut' }}><i /> The two-second hook test <span>10:30</span></motion.div>
    </div>
  )
}

function ProfileVisual() {
  return (
    <div className="profile-visual">
      <div className="profile-cover"><span>CREATOR PROFILE</span></div>
      <div className="avatar"><UserRound size={34} /></div>
      <div className="profile-copy"><h4>Maya Kapoor</h4><p>Video essays about design, culture, and the internet.</p><span><Instagram size={14} /> @mayamakes</span></div>
      <div className="profile-stats"><div><b>34</b><small>SCRIPTS</small></div><div><b>7</b><small>READY</small></div><div><b>4</b><small>CATEGORIES</small></div></div>
    </div>
  )
}

function Features() {
  return (
    <section className="features section-pad" id="features">
      <div className="page-width">
        <Reveal className="section-heading centered">
          <p className="eyebrow">BUILT FOR THE WORK BEFORE “RECORD”</p>
          <h2>Everything you need <em>before you press record.</em></h2>
          <p>Write, organize, and schedule your scripts without stitching together notes, documents, and calendars.</p>
        </Reveal>

        <div className="feature-row feature-row--library">
          <Reveal className="feature-copy">
            <span className="feature-icon"><FolderOpen /></span>
            <p className="eyebrow">SCRIPT LIBRARY</p>
            <h3>Find the right script in seconds.</h3>
            <p>Keep every title, description, category, and idea together. Open the next script without searching through scattered documents.</p>
            <a href="#start">Organize your scripts <ArrowRight size={17} /></a>
          </Reveal>
          <Reveal className="feature-visual-wrap" delay={0.12}><LibraryVisual /></Reveal>
        </div>

        <div className="feature-row feature-row--reverse">
          <Reveal className="feature-copy">
            <span className="feature-icon feature-icon--blue"><CalendarDays /></span>
            <p className="eyebrow">SHOOT-DAY PLANNING</p>
            <h3>Turn finished scripts into a shoot plan.</h3>
            <p>Add a shoot date to any script and see what is ready to record. Your writing queue becomes a production plan you can follow.</p>
            <a href="#start">Plan a shoot day <ArrowRight size={17} /></a>
          </Reveal>
          <Reveal className="feature-visual-wrap" delay={0.12}><CalendarVisual /></Reveal>
        </div>

        <div className="feature-row">
          <Reveal className="feature-copy">
            <span className="feature-icon feature-icon--yellow"><UserRound /></span>
            <p className="eyebrow">CREATOR WORKSPACE</p>
            <h3>A workspace organized your way.</h3>
            <p>Create the categories that match your channel, format, or workflow, and keep them connected to your creator profile.</p>
            <a href="#start">Build your workspace <ArrowRight size={17} /></a>
          </Reveal>
          <Reveal className="feature-visual-wrap" delay={0.12}><ProfileVisual /></Reveal>
        </div>
      </div>
    </section>
  )
}

const useCases = {
  YouTube: { title: 'Keep long-form ideas clear from hook to close.', description: 'Keep the angle, story beats, notes, and shoot date attached to one easy-to-find script.', tag: '8 MIN READ', color: '#ef5b45' },
  Reels: { title: 'Get quick ideas ready while they are still fresh.', description: 'Capture the opening, shape the short script, and add it to your next shoot day.', tag: '30 SEC', color: '#6686df' },
  Tutorials: { title: 'Put every step in order before you record.', description: 'Keep explanations and supporting notes in the exact sequence your audience needs.', tag: 'STEP BY STEP', color: '#e7b846' },
  'Brand content': { title: 'Keep the brief, the script, and the deadline together.', description: 'Give every campaign script a clear home and know what is ready for production.', tag: 'CAMPAIGN', color: '#b587d7' },
}

function UseCases() {
  const [active, setActive] = useState('YouTube')
  const item = useCases[active]
  return (
    <section className="use-cases section-pad" id="creators">
      <div className="page-width">
        <Reveal className="section-heading section-heading--split">
          <div><p className="eyebrow eyebrow--light">FOR EVERY KIND OF CREATOR</p><h2>Made for the way <em>you create.</em></h2></div>
          <p>Select a format to see how the same simple workflow fits your content.</p>
        </Reveal>
        <div className="use-case-tabs" role="tablist" aria-label="Creator formats">
          {Object.keys(useCases).map(name => <button key={name} onClick={() => setActive(name)} className={active === name ? 'active' : ''} role="tab" aria-selected={active === name}>{name}</button>)}
        </div>
        <AnimatePresence mode="wait">
          <motion.div className="use-case-stage" key={active} initial={{ opacity: 0, y: 18 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: -12 }} transition={{ duration: 0.4, ease }}>
            <div className="use-case-copy"><span className="format-dot" style={{ background: item.color }} /><small>{item.tag}</small><h3>{item.title}</h3><p>{item.description}</p></div>
            <div className="use-case-script">
              <div className="paper-holes"><i /><i /><i /><i /><i /></div>
              <p className="paper-label" style={{ color: item.color }}>{active.toUpperCase()} SCRIPT</p>
              <h4>{active === 'Reels' ? 'The two-second hook test' : active === 'Tutorials' ? 'Build a better creative system' : active === 'Brand content' ? 'A desk made for deep work' : 'Why your best ideas arrive too late'}</h4>
              <b>OPENING</b>
              <p>There is a moment every creator knows: the idea is clear, but the page is still empty.</p>
              <div className="paper-lines"><i /><i /><i /><i /></div>
              <div className="paper-footer"><span style={{ background: item.color }}>{active}</span><span>Ready to shoot <Check size={14} /></span></div>
            </div>
          </motion.div>
        </AnimatePresence>
      </div>
    </section>
  )
}

function Steps() {
  const steps = [
    ['01', 'Capture the idea', 'Start with a title, a rough thought, or the opening line you do not want to forget.'],
    ['02', 'Shape the script', 'Add context, keep the writing focused, and organize it in the right category.'],
    ['03', 'Schedule the shoot', 'Pick a shoot day and move from “someday” to the next thing you will record.'],
  ]
  return (
    <section className="steps section-pad">
      <div className="page-width">
        <Reveal className="section-heading centered"><p className="eyebrow">A SIMPLE CREATIVE PROCESS</p><h2>Your next shoot in <em>three clear steps.</em></h2></Reveal>
        <div className="steps-grid">
          {steps.map(([number, title, copy], index) => <Reveal className="step-card" delay={index * 0.1} key={number}><span>{number}</span><div className="step-icon">{index === 0 ? <Sparkles /> : index === 1 ? <FolderOpen /> : <Film />}</div><h3>{title}</h3><p>{copy}</p></Reveal>)}
        </div>
      </div>
    </section>
  )
}

function FinalCTA() {
  return (
    <section className="final-cta" id="start">
      <div className="cta-marquee" aria-hidden="true"><div>WRITE IT · SHAPE IT · SHOOT IT · WRITE IT · SHAPE IT · SHOOT IT ·</div></div>
      <div className="page-width cta-content">
        <Reveal><p className="eyebrow eyebrow--light">YOUR NEXT VIDEO STARTS HERE</p><h2>Ready when <em>the camera is.</em></h2><p>Write the script, choose a shoot day, and open it when it is time to record.</p><a className="button button--cream" href="#signup">Start writing <ArrowRight size={18} /></a><small>BUILD YOUR CREATOR WORKSPACE</small></Reveal>
        <motion.div className="cta-card-stack" animate={{ y: [0, -10, 0] }} transition={{ duration: 5, repeat: Infinity, ease: 'easeInOut' }} aria-hidden="true"><i /><i /><JourneyCard /></motion.div>
      </div>
    </section>
  )
}

function Footer() {
  return (
    <footer>
      <div className="page-width footer-main">
        <div><Mark /><p>From first thought to shoot day.</p></div>
        <div><b>Product</b><a href="#features">Features</a><a href="#workflow">How it works</a><a href="#creators">For creators</a></div>
        <div><b>Account</b><a href="#signin">Sign in</a><a href="#start">Create account</a></div>
      </div>
      <div className="page-width footer-bottom"><span>© 2026 Script Writer</span><span>Made for creators who are ready to record.</span></div>
    </footer>
  )
}

export default function App() {
  return <><Header /><main><Hero /><Ticker /><ScriptJourney /><Features /><UseCases /><Steps /><FinalCTA /></main><Footer /></>
}
