import React, { useMemo, useState, useEffect } from 'react';
import {
  createBoard as apiCreateBoard,
  createPin as apiCreatePin,
  fetchBoards,
  fetchBusinessProfiles,
  fetchPins,
  fetchShowcases,
  fetchSponsoredPins,
  fetchInvitations,
  followUser,
  listFollowers,
  listFollowing,
  loginUser,
  registerUser,
  sendInvitation,
  unfollowUser,
  updateInvitation,
} from './api';

const navLinks = ['Home', 'Explore', 'Create'];

const initialPins = [
  {
    id: 1,
    title: 'Cozy reading nook inspiration',
    author: 'Mara Chen',
    avatar:
      'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1505691938895-1758d7feb511?auto=format&fit=crop&w=900&q=80',
    link: 'studiohaus.com/reading-nooks',
    tag: 'Home decor',
    keywords: ['reading', 'nook', 'cozy'],
    boardId: 1,
    visibility: 'PUBLIC',
  },
  {
    id: 2,
    title: 'Weekend brunch board',
    author: 'Priya Kapoor',
    avatar:
      'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=900&q=80',
    link: 'gatherandgraze.com',
    tag: 'Food & drink',
    keywords: ['brunch', 'board', 'food'],
    boardId: 2,
    visibility: 'PUBLIC',
  },
  {
    id: 3,
    title: 'Minimalist workspace grid',
    author: 'Oskar Niemi',
    avatar:
      'https://images.unsplash.com/photo-1531123897727-8f129e1688ce?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1516383740770-fbcc5ccbece0?auto=format&fit=crop&w=900&q=80',
    link: 'dribbble.com/oskdesign',
    tag: 'DIY',
    keywords: ['workspace', 'grid', 'desk'],
    boardId: 3,
    visibility: 'PUBLIC',
  },
  {
    id: 4,
    title: 'Backyard firepit lounge',
    author: 'Sofia Martinez',
    avatar:
      'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1505691938895-1758d7feb511?auto=format&fit=crop&w=900&q=80&sat=-30',
    link: 'outdoorliving.co/fire',
    tag: 'Gardening',
    keywords: ['backyard', 'fire', 'patio'],
    boardId: 4,
    visibility: 'PUBLIC',
  },
  {
    id: 5,
    title: 'Mediterranean escape itinerary',
    author: 'Lena Rossi',
    avatar:
      'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1505761671935-60b3a7427bad?auto=format&fit=crop&w=900&q=80',
    link: 'lenatravels.com',
    tag: 'Travel',
    keywords: ['travel', 'mediterranean'],
    boardId: 5,
    visibility: 'PUBLIC',
  },
  {
    id: 6,
    title: 'Modern boho living room palette',
    author: 'Kai Turner',
    avatar:
      'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1484154218962-a197022b5858?auto=format&fit=crop&w=900&q=80',
    link: 'kaiturner.studio',
    tag: 'Home decor',
    keywords: ['living room', 'boho'],
    boardId: 1,
    visibility: 'PUBLIC',
  },
  {
    id: 7,
    title: 'Summer capsule wardrobe',
    author: 'Zara Ali',
    avatar:
      'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1515377905703-c4788e51af15?auto=format&fit=crop&w=900&q=80',
    link: 'zarastyles.com',
    tag: 'Fashion',
    keywords: ['wardrobe', 'summer'],
    boardId: 6,
    visibility: 'PUBLIC',
  },
  {
    id: 8,
    title: 'Plant shelf styling ideas',
    author: 'Theo Laurent',
    avatar:
      'https://images.unsplash.com/photo-1531123897727-8f129e1688ce?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1505691938895-1758d7feb511?auto=format&fit=crop&w=900&q=80&sat=-60',
    link: 'greenroom.blog',
    tag: 'Gardening',
    keywords: ['plants', 'shelf'],
    boardId: 4,
    visibility: 'PRIVATE',
  },
  {
    id: 9,
    title: 'Five minute desk stretch routine',
    author: 'Jamie Patel',
    avatar:
      'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1518611012118-696072aa579a?auto=format&fit=crop&w=900&q=80',
    link: 'wellnest.com/stretch',
    tag: 'Wellness',
    keywords: ['stretch', 'desk'],
    boardId: 7,
    visibility: 'PUBLIC',
  },
  {
    id: 10,
    title: 'A-frame cabin escape',
    author: 'Miles Ortega',
    avatar:
      'https://images.unsplash.com/photo-1472220625704-91e1462799b2?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=900&q=80',
    link: 'northwoods.com',
    tag: 'Travel',
    keywords: ['cabin', 'mountain'],
    boardId: 5,
    visibility: 'PUBLIC',
  },
  {
    id: 11,
    title: 'Moody kitchen makeover',
    author: 'Nia Moreau',
    avatar:
      'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1493663284031-b7e3aefcae8e?auto=format&fit=crop&w=900&q=80',
    link: 'ateliermoreau.com',
    tag: 'Home decor',
    keywords: ['kitchen', 'makeover'],
    boardId: 1,
    visibility: 'PUBLIC',
  },
  {
    id: 12,
    title: 'Urban balcony jungle',
    author: 'Ava Collins',
    avatar:
      'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=100&q=80',
    image:
      'https://images.unsplash.com/photo-1505691938895-1758d7feb511?auto=format&fit=crop&w=900&q=80&sat=-20',
    link: 'smallspaces.com',
    tag: 'Gardening',
    keywords: ['balcony', 'plants'],
    boardId: 4,
    visibility: 'PUBLIC',
  },
];

const starterBoards = [
  { id: 1, title: 'Interiors', description: 'Warm minimal home decor', visibility: 'PUBLIC' },
  { id: 2, title: 'Recipes', description: 'Meals to try soon', visibility: 'PUBLIC' },
  { id: 3, title: 'Workspace', description: 'Desk inspiration', visibility: 'PUBLIC' },
  { id: 4, title: 'Garden', description: 'Plants and balconies', visibility: 'PRIVATE' },
  { id: 5, title: 'Trips', description: 'Dream travel boards', visibility: 'PUBLIC' },
  { id: 6, title: 'Style', description: 'Wardrobe notes', visibility: 'PUBLIC' },
  { id: 7, title: 'Wellness', description: 'Stretching, yoga', visibility: 'PUBLIC' },
];

const businessProfiles = [
  {
    id: 'b1',
    name: 'Casa Studio',
    logo:
      'https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=120&q=80',
    description: 'Sustainable home goods and furniture.',
    link: 'https://casastudio.example.com',
    showcases: ['New ceramics', 'Soft linens'],
  },
  {
    id: 'b2',
    name: 'Trailblaze Co.',
    logo:
      'https://images.unsplash.com/photo-1470229538611-16ba8c7ffbd7?auto=format&fit=crop&w=120&q=80',
    description: 'Outdoor gear engineered for adventure.',
    link: 'https://trailblaze.example.com',
    showcases: ['Summer hikes', 'Ultralight packs'],
  },
];

const sponsoredPins = [
  {
    id: 's1',
    title: 'V60 Brew Guide',
    brand: 'North Roast',
    image:
      'https://images.unsplash.com/photo-1470337458703-46ad1756a187?auto=format&fit=crop&w=900&q=80',
    url: 'northroast.com/v60',
  },
  {
    id: 's2',
    title: 'Minimal sneakers drop',
    brand: 'Form Athletics',
    image:
      'https://images.unsplash.com/photo-1460353581641-37baddab0fa2?auto=format&fit=crop&w=900&q=80',
    url: 'form.run/drop',
  },
];

const invitationsSeed = [
  {
    id: 'inv-1',
    from: 'Kai Turner',
    board: 'Interiors',
    type: 'board collaboration',
    status: 'PENDING',
    note: 'Join to curate new living room looks',
  },
  {
    id: 'inv-2',
    from: 'Casa Studio',
    board: 'Sponsored content',
    type: 'connection',
    status: 'PENDING',
    note: 'Partner on a fall launch series',
  },
];

const followersSeed = [
  { id: 'u1', name: 'Theo Laurent', avatar: 'https://images.unsplash.com/photo-1531123897727-8f129e1688ce?auto=format&fit=crop&w=80&q=80' },
  { id: 'u2', name: 'Priya Kapoor', avatar: 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=80&q=80' },
];

const followingSeed = [
  { id: 'u3', name: 'Mara Chen', avatar: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=80&q=80' },
  { id: 'u4', name: 'Zara Ali', avatar: 'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=80&q=80' },
  { id: 'u5', name: 'Nia Moreau', avatar: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=80&q=80' },
];

const pinFilters = ['Home decor', 'Travel', 'Food & drink', 'DIY', 'Fashion', 'Gardening', 'Wellness'];

const validateEmail = (value) => /.+@.+\.(com|org|net|io|in)$/i.test(value || '');
const validateUsername = (value) => /^[a-z0-9._-]+$/.test(value || '');
const validatePassword = (value) =>
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,16}$/.test(value || '');

const mapPinsToCards = (apiPins = [], apiBoards = []) => {
  const boardLookup = Object.fromEntries(apiBoards.map((b) => [b.id, b.title]));
  return apiPins.map((pin) => ({
    id: pin.id,
    title: pin.title,
    author: pin.boardName || 'Pinned',
    avatar:
      'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=100&q=80',
    image: pin.mediaUrl,
    link: pin.sourceUrl || '',
    tag: boardLookup[pin.boardId] || 'Saved',
    keywords: pin.keywords || [],
    boardId: pin.boardId,
    visibility: pin.visible ? 'PUBLIC' : 'PRIVATE',
  }));
};

export default function App() {
  const [nav, setNav] = useState('Home');
  const [pins, setPins] = useState(initialPins);
  const [boards, setBoards] = useState(starterBoards);
  const [search, setSearch] = useState('');
  const [selectedFilter, setSelectedFilter] = useState('');
  const [authMode, setAuthMode] = useState('login');
  const [authErrors, setAuthErrors] = useState({});
  const [authForm, setAuthForm] = useState({ email: '', username: '', password: '', confirm: '' });
  const [activeUser, setActiveUser] = useState(null);
  const [draftPin, setDraftPin] = useState({ title: '', link: '', tag: pinFilters[0], boardId: starterBoards[0].id, visibility: 'PUBLIC' });
  const [draftBoard, setDraftBoard] = useState({ title: '', description: '', visibility: 'PUBLIC' });
  const [followers, setFollowers] = useState([]);
  const [following, setFollowing] = useState([]);
  const [invitations, setInvitations] = useState([]);
  const [saves, setSaves] = useState([]);
  const [loginFailures, setLoginFailures] = useState([]);
  const [circuitOpenUntil, setCircuitOpenUntil] = useState(null);
  const [businesses, setBusinesses] = useState([]);
  const [showcases, setShowcases] = useState([]);
  const [sponsored, setSponsored] = useState([]);
  const [statusMessage, setStatusMessage] = useState('');

  useEffect(() => {
    const loadStaticData = async () => {
      try {
        const [fetchedBoards, fetchedPins, biz, showcaseList, sponsoredList] = await Promise.all([
          fetchBoards(),
          fetchPins(),
          fetchBusinessProfiles(),
          fetchShowcases(),
          fetchSponsoredPins(),
        ]);
        setBoards(fetchedBoards.length ? fetchedBoards : starterBoards);
        setPins(fetchedPins.length ? mapPinsToCards(fetchedPins, fetchedBoards) : initialPins);
        setBusinesses(biz);
        setShowcases(showcaseList);
        setSponsored(sponsoredList);
        setDraftPin((prev) => ({ ...prev, boardId: (fetchedBoards[0] || starterBoards[0]).id }));
      } catch (error) {
        console.error('Falling back to starter content', error);
        setBoards(starterBoards);
        setPins(initialPins);
        setBusinesses(businessProfiles);
        setShowcases([]);
        setSponsored([]);
      }
    };
    loadStaticData();
  }, []);

  useEffect(() => {
    const now = Date.now();
    if (loginFailures.length >= 3) {
      const windowStart = loginFailures[loginFailures.length - 3];
      if (now - windowStart < 30000) {
        const lockUntil = now + 60000;
        setCircuitOpenUntil(lockUntil);
      }
    }
  }, [loginFailures]);

  useEffect(() => {
    if (!circuitOpenUntil) return undefined;
    const timer = setInterval(() => {
      if (Date.now() >= circuitOpenUntil) {
        setCircuitOpenUntil(null);
        setLoginFailures([]);
      }
    }, 500);
    return () => clearInterval(timer);
  }, [circuitOpenUntil]);

  useEffect(() => {
    if (!activeUser?.id) {
      setFollowers(followersSeed);
      setFollowing(followingSeed);
      setInvitations(invitationsSeed);
      return;
    }
    const loadSocial = async () => {
      try {
        const [fetchedFollowers, fetchedFollowing, fetchedInvites] = await Promise.all([
          listFollowers(activeUser.id),
          listFollowing(activeUser.id),
          fetchInvitations(activeUser.id),
        ]);
        setFollowers(fetchedFollowers);
        setFollowing(fetchedFollowing);
        setInvitations(fetchedInvites);
      } catch (error) {
        console.warn('Using seed social data because API failed', error);
        setFollowers(followersSeed);
        setFollowing(followingSeed);
        setInvitations(invitationsSeed);
      }
    };
    loadSocial();
  }, [activeUser?.id]);

  const filteredPins = useMemo(() => {
    return pins.filter((pin) => {
      if (selectedFilter && pin.tag !== selectedFilter) return false;
      if (pin.visibility === 'PRIVATE' && !saves.includes(pin.id)) return false;
      if (!search) return true;
      const keywords = Array.isArray(pin.keywords) ? pin.keywords.join(' ') : '';
      const haystack = `${pin.title} ${pin.tag} ${keywords} ${pin.link || ''}`.toLowerCase();
      return haystack.includes(search.toLowerCase());
    });
  }, [pins, search, selectedFilter, saves]);

  const visibleBoards = useMemo(() => {
    const term = search.toLowerCase();
    return boards.filter((board) => {
      const matches = `${board.title} ${board.description}`.toLowerCase().includes(term);
      const visibilityAllowed = board.visibility === 'PUBLIC' || !!activeUser;
      return matches && visibilityAllowed;
    });
  }, [boards, search, activeUser]);

  const searchResults = useMemo(() => {
    if (!search) return [];
    const pinMatches = filteredPins.map((pin) => ({ type: 'pin', title: pin.title, by: pin.author, id: pin.id }));
    const boardMatches = visibleBoards.map((board) => ({ type: 'board', title: board.title, by: 'You', id: board.id }));
    return [...pinMatches, ...boardMatches];
  }, [filteredPins, visibleBoards, search]);

  const handleAuthChange = (field, value) => {
    setAuthForm((prev) => ({ ...prev, [field]: value }));
  };

  const validateAuth = () => {
    const nextErrors = {};
    if (!validateEmail(authForm.email)) nextErrors.email = 'Please provide a valid email';
    if (authMode === 'register' && !validateUsername(authForm.username)) {
      nextErrors.username = 'Use lowercase letters, digits, dots, dashes, or underscores';
    }
    if (!validatePassword(authForm.password)) {
      nextErrors.password = '8-16 chars incl. upper, lower, digit, special';
    }
    if (authMode === 'register' && authForm.password !== authForm.confirm) {
      nextErrors.confirm = 'Passwords do not match';
    }
    setAuthErrors(nextErrors);
    return Object.keys(nextErrors).length === 0;
  };

  const submitAuth = async (evt) => {
    evt.preventDefault();
    if (circuitOpenUntil && Date.now() < circuitOpenUntil) {
      setAuthErrors({ circuit: 'Circuit open — please wait before another attempt.' });
      return;
    }
    const valid = validateAuth();
    if (!valid) {
      if (authMode === 'login') setLoginFailures((prev) => [...prev, Date.now()]);
      return;
    }
    setAuthErrors({});
    setStatusMessage('');
    try {
      if (authMode === 'register') {
        const registered = await registerUser({
          email: authForm.email,
          username: authForm.username,
          password: authForm.password,
        });
        setActiveUser(registered);
        setStatusMessage('Registration successful.');
      } else {
        const result = await loginUser({ email: authForm.email, password: authForm.password });
        setActiveUser(result.user);
        setStatusMessage(result.message);
      }
      setLoginFailures([]);
      setCircuitOpenUntil(null);
    } catch (error) {
      if (error.status === 429 && error.payload?.retryAfterSeconds) {
        const lockUntil = Date.now() + error.payload.retryAfterSeconds * 1000;
        setCircuitOpenUntil(lockUntil);
        setAuthErrors({ circuit: error.payload.error });
      } else {
        setAuthErrors({ circuit: error.message });
      }
      if (authMode === 'login') setLoginFailures((prev) => [...prev, Date.now()]);
    }
  };

  const createBoard = async (evt) => {
    evt.preventDefault();
    if (!draftBoard.title.trim()) return;
    try {
      const created = await apiCreateBoard({
        title: draftBoard.title.trim(),
        description: draftBoard.description.trim(),
        visibility: draftBoard.visibility,
      });
      setBoards((prev) => [created, ...prev]);
      setDraftBoard({ title: '', description: '', visibility: 'PUBLIC' });
      setStatusMessage('Board created');
    } catch (error) {
      console.error(error);
      setStatusMessage('Unable to create board. Falling back locally.');
      const newBoard = {
        id: Date.now(),
        title: draftBoard.title.trim(),
        description: draftBoard.description.trim(),
        visibility: draftBoard.visibility,
      };
      setBoards((prev) => [newBoard, ...prev]);
    }
  };

  const createPin = async (evt) => {
    evt.preventDefault();
    if (!draftPin.title.trim() || !draftPin.link.trim()) return;
    try {
      const created = await apiCreatePin({
        title: draftPin.title.trim(),
        description: draftPin.title.trim(),
        mediaUrl: draftPin.link.trim(),
        sourceUrl: draftPin.link.trim(),
        keywords: draftPin.title.split(' '),
        boardId: Number(draftPin.boardId),
        draft: false,
        visible: draftPin.visibility !== 'PRIVATE',
      });
      setPins((prev) => [{
        ...created,
        author: activeUser?.username || 'You',
        avatar: 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=100&q=80',
        image: created.mediaUrl,
        tag: draftPin.tag,
        keywords: created.keywords || [],
        link: created.sourceUrl,
        visibility: created.visible ? 'PUBLIC' : 'PRIVATE',
      }, ...prev]);
      setDraftPin({ title: '', link: '', tag: pinFilters[0], boardId: draftPin.boardId, visibility: 'PUBLIC' });
      setStatusMessage('Pin saved');
    } catch (error) {
      console.error(error);
      setStatusMessage('Unable to create pin. Falling back locally.');
      const newPin = {
        id: Date.now(),
        title: draftPin.title.trim(),
        author: activeUser?.username || 'You',
        avatar:
          'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=100&q=80',
        image:
          'https://images.unsplash.com/photo-1493663284031-b7e3aefcae8e?auto=format&fit=crop&w=900&q=80',
        link: draftPin.link.trim(),
        tag: draftPin.tag,
        keywords: draftPin.title.split(' '),
        boardId: Number(draftPin.boardId),
        visibility: draftPin.visibility,
      };
      setPins((prev) => [newPin, ...prev]);
    }
  };

  const toggleSave = (pinId) => {
    setSaves((prev) => (prev.includes(pinId) ? prev.filter((id) => id !== pinId) : [...prev, pinId]));
  };

  const respondInvite = async (id, status) => {
    try {
      await updateInvitation(id, status.toLowerCase());
      const updated = await fetchInvitations(activeUser?.id);
      setInvitations(updated);
    } catch (error) {
      console.warn('Falling back locally', error);
      setInvitations((prev) => prev.map((inv) => (inv.id === id ? { ...inv, status } : inv)));
    }
  };

  const toggleFollow = async (userId, name, avatar) => {
    if (!activeUser?.id) {
      setFollowing((prev) => [...prev, { id: userId, name, avatar }]);
      return;
    }
    try {
      if (following.find((f) => f.id === userId)) {
        await unfollowUser(activeUser.id, userId);
        setFollowing((prev) => prev.filter((f) => f.id !== userId));
      } else {
        await followUser(activeUser.id, userId);
        setFollowing((prev) => [...prev, { id: userId, name, avatar }]);
      }
    } catch (error) {
      console.warn('Follow fallback', error);
      if (following.find((f) => f.id === userId)) {
        setFollowing((prev) => prev.filter((f) => f.id !== userId));
      } else {
        setFollowing((prev) => [...prev, { id: userId, name, avatar }]);
      }
    }
  };

  const logout = () => {
    setActiveUser(null);
    setSaves([]);
  };

  const circuitSecondsLeft = circuitOpenUntil ? Math.max(0, Math.ceil((circuitOpenUntil - Date.now()) / 1000)) : 0;

  return (
    <div className="page">
      <header className="topbar">
        <div className="logo">P</div>
        <nav className="nav">
          {navLinks.map((link) => (
            <button key={link} className={nav === link ? 'nav-link active' : 'nav-link'} onClick={() => setNav(link)}>
              {link}
            </button>
          ))}
        </nav>
        <div className="search">
          <span className="search-icon" aria-hidden>
            🔍
          </span>
          <input value={search} onChange={(e) => setSearch(e.target.value)} placeholder="Search pins or boards" />
        </div>
        <div className="actions">
          <button className="icon-btn" aria-label="Notifications">
            🔔
          </button>
          <button className="icon-btn" aria-label="Messages">
            💬
          </button>
          {activeUser ? (
            <button className="profile" onClick={logout} title="Log out">
              {activeUser.username?.slice(0, 2).toUpperCase()}
            </button>
          ) : (
            <button className="profile" onClick={() => setAuthMode('login')}>⚙</button>
          )}
        </div>
      </header>

      <section className="hero">
        <div className="hero-text">
          <p className="eyebrow">Welcome back{activeUser ? `, ${activeUser.username}` : ''}</p>
          <h1>Find your next idea</h1>
          <p className="subhead">
            Browse inspiration, save drafts, collaborate on boards, and discover business showcases across Pinterest.
          </p>
          <div className="chips">
            {pinFilters.map((filter) => (
              <button
                key={filter}
                className={selectedFilter === filter ? 'chip active' : 'chip'}
                onClick={() => setSelectedFilter(selectedFilter === filter ? '' : filter)}
              >
                {filter}
              </button>
            ))}
          </div>
        </div>
        <div className="hero-preview">
          <div className="stack">
            {pins.slice(0, 3).map((pin) => (
              <img key={pin.id} src={pin.image} alt={pin.title} loading="lazy" />
            ))}
          </div>
        </div>
      </section>

      <section className="panel-grid">
        <div className="panel auth">
          <div className="panel-header">
            <h3>{authMode === 'login' ? 'Log in' : 'Create account'}</h3>
            <div className="switcher">
              <button className={authMode === 'login' ? 'pill active' : 'pill'} onClick={() => setAuthMode('login')}>
                Login
              </button>
              <button className={authMode === 'register' ? 'pill active' : 'pill'} onClick={() => setAuthMode('register')}>
                Register
              </button>
            </div>
          </div>
          <form className="form" onSubmit={submitAuth}>
            <label>
              Email
              <input
                value={authForm.email}
                onChange={(e) => handleAuthChange('email', e.target.value)}
                placeholder="you@example.com"
                required
              />
              {authErrors.email && <span className="error">{authErrors.email}</span>}
            </label>
            {authMode === 'register' && (
              <label>
                Username
                <input
                  value={authForm.username}
                  onChange={(e) => handleAuthChange('username', e.target.value)}
                  placeholder="only lowercase letters, digits, symbols"
                  required
                />
                {authErrors.username && <span className="error">{authErrors.username}</span>}
              </label>
            )}
            <label>
              Password
              <input
                type="password"
                value={authForm.password}
                onChange={(e) => handleAuthChange('password', e.target.value)}
                required
                placeholder="8-16 chars with A-Z, a-z, number, special"
              />
              {authErrors.password && <span className="error">{authErrors.password}</span>}
            </label>
            {authMode === 'register' && (
              <label>
                Confirm password
                <input
                  type="password"
                  value={authForm.confirm}
                  onChange={(e) => handleAuthChange('confirm', e.target.value)}
                  required
                />
                {authForm.confirm && authForm.password !== authForm.confirm && (
                  <span className="error">Passwords do not match</span>
                )}
                {authErrors.confirm && <span className="error">{authErrors.confirm}</span>}
              </label>
            )}
            {authErrors.circuit && <div className="error">{authErrors.circuit}</div>}
            {circuitSecondsLeft > 0 && (
              <div className="timer">Login circuit open · wait {circuitSecondsLeft}s</div>
            )}
            <button type="submit" className="primary">{authMode === 'login' ? 'Log in' : 'Register'}</button>
            {statusMessage && <div className="success">{statusMessage}</div>}
          </form>
        </div>

        <div className="panel">
          <div className="panel-header">
            <h3>Create a new Pin</h3>
            <span className="badge">Draft</span>
          </div>
          <form className="form" onSubmit={createPin}>
            <label>
              Title
              <input
                value={draftPin.title}
                onChange={(e) => setDraftPin((p) => ({ ...p, title: e.target.value }))}
                placeholder="Describe your pin"
                required
              />
            </label>
            <label>
              Destination link
              <input
                value={draftPin.link}
                onChange={(e) => setDraftPin((p) => ({ ...p, link: e.target.value }))}
                placeholder="https://"
                required
              />
            </label>
            <div className="split">
              <label>
                Board
                <select
                  value={draftPin.boardId}
                  onChange={(e) => setDraftPin((p) => ({ ...p, boardId: e.target.value }))}
                >
                  {boards.map((board) => (
                    <option key={board.id} value={board.id}>
                      {board.title}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Topic
                <select value={draftPin.tag} onChange={(e) => setDraftPin((p) => ({ ...p, tag: e.target.value }))}>
                  {pinFilters.map((filter) => (
                    <option key={filter} value={filter}>
                      {filter}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Visibility
                <select
                  value={draftPin.visibility}
                  onChange={(e) => setDraftPin((p) => ({ ...p, visibility: e.target.value }))}
                >
                  <option value="PUBLIC">Public</option>
                  <option value="PRIVATE">Private</option>
                </select>
              </label>
            </div>
            <button className="primary" type="submit">
              Save Pin
            </button>
          </form>
        </div>

        <div className="panel">
          <div className="panel-header">
            <h3>Create a board</h3>
            <span className="badge">Organize</span>
          </div>
          <form className="form" onSubmit={createBoard}>
            <label>
              Title
              <input
                value={draftBoard.title}
                onChange={(e) => setDraftBoard((p) => ({ ...p, title: e.target.value }))}
                required
              />
            </label>
            <label>
              Description
              <input
                value={draftBoard.description}
                onChange={(e) => setDraftBoard((p) => ({ ...p, description: e.target.value }))}
              />
            </label>
            <label>
              Visibility
              <select
                value={draftBoard.visibility}
                onChange={(e) => setDraftBoard((p) => ({ ...p, visibility: e.target.value }))}
              >
                <option value="PUBLIC">Public</option>
                <option value="PRIVATE">Private</option>
              </select>
            </label>
            <button className="primary" type="submit">
              Add board
            </button>
          </form>
        </div>
      </section>

      <section className="panel-grid">
        <div className="panel list">
          <div className="panel-header">
            <h3>Followers</h3>
            <span className="muted">Connections</span>
          </div>
          <ul className="people">
            {followers.map((user) => (
              <li key={user.id}>
                <div className="person">
                  <img src={user.avatar} alt={user.name} />
                  <div>
                    <p>{user.name}</p>
                    <span className="muted">Follower</span>
                  </div>
                </div>
                <button className="pill">View profile</button>
              </li>
            ))}
          </ul>
        </div>
        <div className="panel list">
          <div className="panel-header">
            <h3>Following</h3>
            <span className="muted">Manage</span>
          </div>
          <ul className="people">
            {following.map((user) => (
              <li key={user.id}>
                <div className="person">
                  <img src={user.avatar} alt={user.name} />
                  <div>
                    <p>{user.name}</p>
                    <span className="muted">Following</span>
                  </div>
                </div>
                <button className="pill" onClick={() => toggleFollow(user.id, user.name, user.avatar)}>
                  Unfollow
                </button>
              </li>
            ))}
          </ul>
        </div>
        <div className="panel list">
          <div className="panel-header">
            <h3>Invitations</h3>
            <span className="muted">Collaboration</span>
          </div>
          <ul className="invites">
            {invitations.map((inv) => (
              <li key={inv.id}>
                <div>
                  <p>
                    <strong>{inv.from}</strong> invited you to {inv.board} ({inv.type})
                  </p>
                  <span className="muted">{inv.note}</span>
                </div>
                <div className="invite-actions">
                  <button className="pill" onClick={() => respondInvite(inv.id, 'ACCEPTED')}>
                    Accept
                  </button>
                  <button className="pill" onClick={() => respondInvite(inv.id, 'DECLINED')}>
                    Decline
                  </button>
                  <span className={`status ${inv.status.toLowerCase()}`}>{inv.status}</span>
                </div>
              </li>
            ))}
          </ul>
        </div>
      </section>

      <section className="panel-grid">
        <div className="panel list">
          <div className="panel-header">
            <h3>Boards</h3>
            <span className="muted">Sort & filter</span>
          </div>
          <ul className="boards">
            {visibleBoards.map((board) => (
              <li key={board.id}>
                <div>
                  <p>{board.title}</p>
                  <span className="muted">{board.description || 'No description'}</span>
                </div>
                <span className={`pill ${board.visibility === 'PRIVATE' ? 'ghost' : ''}`}>
                  {board.visibility}
                </span>
              </li>
            ))}
            {!visibleBoards.length && <p className="muted">No boards match your search.</p>}
          </ul>
        </div>
        <div className="panel list">
          <div className="panel-header">
            <h3>Business profiles</h3>
            <span className="muted">Explore brands</span>
          </div>
          <ul className="business">
            {(businesses.length ? businesses : businessProfiles).map((biz) => (
              <li key={biz.id}>
                <div className="person">
                  <img src={biz.logo} alt={biz.name} />
                  <div>
                    <p>{biz.name}</p>
                    <span className="muted">{biz.description}</span>
                    <div className="muted">Showcases: {biz.showcases.join(', ')}</div>
                  </div>
                </div>
                <div className="invite-actions">
                  <a className="pill" href={biz.link} target="_blank" rel="noreferrer">
                    Visit
                  </a>
                  <button className="pill" onClick={() => toggleFollow(biz.id, biz.name, biz.logo)}>
                    {following.find((f) => f.id === biz.id) ? 'Unfollow' : 'Follow'}
                  </button>
                </div>
              </li>
            ))}
          </ul>
        </div>
        <div className="panel list">
          <div className="panel-header">
            <h3>Sponsored Pins</h3>
            <span className="muted">Advertising</span>
          </div>
          <div className="sponsored">
            {(sponsored.length ? sponsored : sponsoredPins).map((ad) => (
              <article key={ad.id} className="sponsored-card">
                <span className="sponsored-label">Sponsored</span>
                <img src={ad.image || ad.mediaUrl} alt={ad.title} />
                <h4>{ad.title}</h4>
                <p className="muted">{ad.brand}</p>
                <a className="pill" href={ad.url} target="_blank" rel="noreferrer">
                  Learn more
                </a>
              </article>
            ))}
          </div>
        </div>
      </section>

      <section className="feed">
        <h2 className="section-title">Pins for you</h2>
        {searchResults.length > 0 && (
          <div className="search-results">
            <p className="muted">Results for "{search}"</p>
            <ul>
              {searchResults.map((result) => (
                <li key={`${result.type}-${result.id}`}>
                  <span className="pill ghost">{result.type}</span>
                  <strong>{result.title}</strong>
                  <span className="muted">by {result.by}</span>
                </li>
              ))}
            </ul>
          </div>
        )}
        <div className="masonry">
          {filteredPins.map((pin) => (
            <article key={pin.id} className="pin-card">
              <div className="pin-image">
                <img src={pin.image} alt={pin.title} loading="lazy" />
                <div className="pin-overlay">
                  <button className="save-btn" onClick={() => toggleSave(pin.id)}>
                    {saves.includes(pin.id) ? 'Saved' : 'Save'}
                  </button>
                  <div className="overlay-actions">
                    <button aria-label="Send" className="circle-btn">
                      ↗
                    </button>
                    <button aria-label="More" className="circle-btn">
                      ···
                    </button>
                  </div>
                </div>
              </div>
              <div className="pin-meta">
                <div>
                  <p className="pin-title">{pin.title}</p>
                  <p className="pin-link">{pin.link}</p>
                </div>
                <div className="author">
                  <img src={pin.avatar} alt={pin.author} />
                  <span>{pin.author}</span>
                </div>
                <div className="meta-row">
                  <span className="pin-tag">{pin.tag}</span>
                  <span className="pin-tag ghost">{pin.visibility}</span>
                </div>
              </div>
            </article>
          ))}
        </div>
      </section>
    </div>
  );
}
