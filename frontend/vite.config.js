import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    port: 5173,
    proxy: {
      '/user': 'http://localhost:8080',
      '/admin': 'http://localhost:8080',
    },
  },
});
