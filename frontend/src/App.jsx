import React from 'react';

const sections = [
  { title: 'Register', description: 'Create a new account to start collecting pins.' },
  { title: 'Login', description: 'Access your boards and saved inspiration.' },
  { title: 'Create Pins', description: 'Upload images or video links, add metadata, and publish.' },
  { title: 'Boards', description: 'Organize pins by topic and collaborate with teammates.' },
  { title: 'Search', description: 'Find pins and boards with keyword search and filters.' },
];

export default function App() {
  return (
    <main className="layout">
      <header className="hero">
        <h1>Pinterest Clone</h1>
        <p>Spring Boot microservices with a React SPA frontend.</p>
      </header>
      <section className="grid">
        {sections.map((item) => (
          <article key={item.title} className="card">
            <h2>{item.title}</h2>
            <p>{item.description}</p>
          </article>
        ))}
      </section>
    </main>
  );
}
