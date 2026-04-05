/* ============================================================
   HomeMitra API Client
   ============================================================ */
const API_BASE = 'http://localhost:8080/api';

const api = {
  _token: () => localStorage.getItem('hm_token'),

  _headers(extra = {}) {
    const h = { 'Content-Type': 'application/json', ...extra };
    const t = this._token();
    if (t) h['Authorization'] = `Bearer ${t}`;
    return h;
  },

  async _req(method, path, body) {
    try {
      const res = await fetch(`${API_BASE}${path}`, {
        method, headers: this._headers(),
        body: body ? JSON.stringify(body) : undefined
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.message || 'Request failed');
      return data;
    } catch (e) {
      Toast.show(e.message, 'error');
      throw e;
    }
  },

  get:    (p)    => api._req('GET', p),
  post:   (p, b) => api._req('POST', p, b),
  put:    (p, b) => api._req('PUT', p, b),
  patch:  (p, b) => api._req('PATCH', p, b),
  delete: (p)    => api._req('DELETE', p),

  // Auth
  auth: {
    register: (b) => api.post('/auth/register', b),
    login:    (b) => api.post('/auth/login', b),
  },

  // Services
  services: {
    featured:    ()     => api.get('/services/featured'),
    all:         (p, s) => api.get(`/services?page=${p||0}&size=${s||12}`),
    byCategory:  (id)   => api.get(`/services/category/${id}`),
    search:      (q)    => api.get(`/services/search?q=${encodeURIComponent(q)}`),
    bySlug:      (slug) => api.get(`/services/${slug}`),
  },

  // Categories
  categories: {
    all: () => api.get('/categories'),
  },

  // Bookings
  bookings: {
    create:       (b)    => api.post('/bookings', b),
    mine:         (p, s) => api.get(`/bookings/my?page=${p||0}&size=${s||10}`),
    get:          (ref)  => api.get(`/bookings/${ref}`),
    updateStatus: (id, status) => api.patch(`/bookings/${id}/status?status=${status}`),
  },

  // Payments
  payments: {
    createOrder: (bookingId) => api.post(`/payments/create-order/${bookingId}`),
    verify:      (b)         => api.post('/payments/verify', b),
  },

  // Notifications
  notifications: {
    list:      () => api.get('/notifications'),
    unread:    () => api.get('/notifications/unread-count'),
    markRead:  () => api.post('/notifications/mark-read'),
  }
};

/* ── Auth helpers ── */
const Auth = {
  save(data) {
    localStorage.setItem('hm_token', data.token);
    localStorage.setItem('hm_refresh', data.refreshToken);
    localStorage.setItem('hm_user', JSON.stringify({
      id: data.userId, name: data.fullName, email: data.email, role: data.role
    }));
  },
  clear() {
    ['hm_token','hm_refresh','hm_user'].forEach(k => localStorage.removeItem(k));
  },
  user() {
    try { return JSON.parse(localStorage.getItem('hm_user')); } catch { return null; }
  },
  loggedIn() { return !!localStorage.getItem('hm_token') && !!this.user(); },
  role()     { return this.user()?.role; },
  requireAuth(redirectTo = '/pages/login.html') {
    if (!this.loggedIn()) { window.location.href = redirectTo; return false; }
    return true;
  },
  requireRole(role, redirectTo = '/index.html') {
    if (this.role() !== role) { window.location.href = redirectTo; return false; }
    return true;
  }
};

/* ── Toast ── */
const Toast = {
  _container: null,
  _init() {
    if (!this._container) {
      this._container = document.createElement('div');
      this._container.className = 'toast-container';
      document.body.appendChild(this._container);
    }
  },
  show(msg, type = 'info', duration = 3500) {
    this._init();
    const icons = { success: '✅', error: '❌', warning: '⚠️', info: 'ℹ️' };
    const t = document.createElement('div');
    t.className = `toast ${type}`;
    t.innerHTML = `<span>${icons[type]||''}</span><span>${msg}</span>`;
    this._container.appendChild(t);
    setTimeout(() => {
      t.style.animation = 'toastOut 0.3s ease forwards';
      setTimeout(() => t.remove(), 300);
    }, duration);
  }
};

/* ── Stars renderer ── */
function renderStars(rating, max = 5) {
  return Array.from({length: max}, (_, i) =>
    `<span class="star${i < Math.round(rating) ? '' : ' empty'}">★</span>`).join('');
}

/* ── Price formatter ── */
function formatPrice(n) {
  return '₹' + Number(n).toLocaleString('en-IN');
}

/* ── Skeleton loaders ── */
function skeletonCard(n = 4) {
  return Array.from({length: n}, () => `
    <div class="card" style="padding:1.5rem">
      <div class="skeleton" style="height:60px;width:60px;border-radius:50%;margin-bottom:1rem"></div>
      <div class="skeleton" style="height:18px;width:70%;margin-bottom:.75rem"></div>
      <div class="skeleton" style="height:12px;width:90%;margin-bottom:.5rem"></div>
      <div class="skeleton" style="height:12px;width:60%"></div>
    </div>`).join('');
}

/* ── Navbar init ── */
function initNavbar() {
  const user = Auth.user();
  const actionsEl = document.getElementById('navActions');
  if (!actionsEl) return;

  if (user) {
    actionsEl.innerHTML = `
      <button class="btn btn-ghost btn-icon notif-btn" id="notifBtn" onclick="window.location.href='/pages/notifications.html'">
        🔔 <span class="notif-badge hidden" id="notifCount">0</span>
      </button>
      <div class="user-avatar" onclick="window.location.href='/pages/dashboard.html'" title="${user.name}">
        ${user.name.charAt(0).toUpperCase()}
      </div>
      <button class="btn btn-ghost btn-sm" onclick="logout()">Logout</button>`;

    // Load unread count
    api.notifications.unread().then(r => {
      const cnt = r.data;
      const badge = document.getElementById('notifCount');
      if (badge && cnt > 0) { badge.textContent = cnt; badge.classList.remove('hidden'); }
    }).catch(() => {});
  } else {
    actionsEl.innerHTML = `
      <a href="/pages/login.html" class="btn btn-secondary btn-sm">Login</a>
      <a href="/pages/register.html" class="btn btn-primary btn-sm">Sign Up</a>`;
  }

  // Scroll effect
  window.addEventListener('scroll', () => {
    document.querySelector('.navbar')?.classList.toggle('scrolled', window.scrollY > 10);
  });

  // Hamburger
  const burger = document.getElementById('hamburger');
  const navLinks = document.getElementById('navLinks');
  burger?.addEventListener('click', () => navLinks?.classList.toggle('open'));
}

function logout() {
  Auth.clear();
  Toast.show('Logged out successfully', 'success');
  setTimeout(() => window.location.href = '/index.html', 800);
}

/* ── WebSocket for tracking ── */
function connectTrackingSocket(bookingId, onUpdate) {
  if (typeof SockJS === 'undefined' || typeof Stomp === 'undefined') return;
  const socket = new SockJS('http://localhost:8080/ws');
  const client = Stomp.over(socket);
  client.connect({}, () => {
    client.subscribe(`/topic/booking/${bookingId}`, (msg) => {
      try { onUpdate(JSON.parse(msg.body)); } catch {}
    });
  });
  return client;
}
