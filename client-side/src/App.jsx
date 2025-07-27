import { useState } from 'react'
import './App.css'
import SignupForm from './components/auth/SignupForm.jsx'

function App() {
  const [currentView, setCurrentView] = useState('home')

  return (
    <>
      <div style={{ padding: '20px' }}>
        <nav style={{ marginBottom: '20px' }}>
          <button
            onClick={() => setCurrentView('home')}
            style={{ marginRight: '10px', padding: '8px 16px' }}
          >
            Home
          </button>
          <button
            onClick={() => setCurrentView('signup')}
            style={{ padding: '8px 16px' }}
          >
            Signup
          </button>
        </nav>

        {currentView === 'home' && (
          <div>
            <h1>OM VaultChain</h1>
            <p>Welcome to the secure file management system.</p>
          </div>
        )}

        {currentView === 'signup' && <SignupForm />}
      </div>
    </>
  )
}

export default App
