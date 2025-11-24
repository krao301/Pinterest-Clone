import React from 'react';

const navLinks = [
  { label: 'Home', active: true },
  { label: 'Explore' },
  { label: 'Create' },
];

const pinFilters = [
  'Home decor',
  'Travel',
  'Food & drink',
  'DIY',
  'Fashion',
  'Gardening',
  'Wellness',
];

const pins = [
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
  },
];

export default function App() {
  return (
    <div className="page">
      <header className="topbar">
        <div className="logo">P</div>
        <nav className="nav">
          {navLinks.map((link) => (
            <button key={link.label} className={link.active ? 'nav-link active' : 'nav-link'}>
              {link.label}
            </button>
          ))}
        </nav>
        <div className="search">
          <span className="search-icon" aria-hidden>🔍</span>
          <input placeholder="Search for easy dinner, home office, summer outfits..." />
        </div>
        <div className="actions">
          <button className="icon-btn" aria-label="Notifications">
            🔔
          </button>
          <button className="icon-btn" aria-label="Messages">
            💬
          </button>
          <button className="profile">MC</button>
        </div>
      </header>

      <section className="hero">
        <div className="hero-text">
          <p className="eyebrow">Welcome back, Mara</p>
          <h1>Find your next idea</h1>
          <p className="subhead">
            Dive into the latest saves from creators you follow and discover trends curated for you.
          </p>
          <div className="chips">
            {pinFilters.map((filter) => (
              <button key={filter} className="chip">
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

      <section className="feed">
        <h2 className="section-title">Inspired by your recent saves</h2>
        <div className="masonry">
          {pins.map((pin) => (
            <article key={pin.id} className="pin-card">
              <div className="pin-image">
                <img src={pin.image} alt={pin.title} loading="lazy" />
                <div className="pin-overlay">
                  <button className="save-btn">Save</button>
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
                <span className="pin-tag">{pin.tag}</span>
              </div>
            </article>
          ))}
        </div>
      </section>
    </div>
  );
}
