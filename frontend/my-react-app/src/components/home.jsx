import React, { useState } from 'react'
import Create from '../assets/create.png';
import HomeIcon from '../assets/home.png';
import Notification from '../assets/notification.png';
import Setting from '../assets/setting.png';
import Calendar from '../assets/calendar.png';


function Home() {
  const [active, setActive] = useState('Home');

  const icons = [
    { name: 'Home', src: HomeIcon },
    { name: 'Create', src: Create ,direct:'/postCreation'},
    { name: 'Notification', src: Notification },
    { name: 'Calendar', src: Calendar },
    { name: 'Setting', src: Setting },
  ];

  return (
    <div>
        {/* sidebar */}
        <aside className="w-16 h-screen bg-gradient-to-br from-[#76C893] to-[#1E6091] text-white flex flex-col items-center py-4 shadow-md">
          <div className="sidebar-icon text-2xl mb-10 cursor-pointer">Logo</div>

          {icons.map((icon) => (
            <div
              key={icon.name}
              onClick={() => setActive(icon.name)}
              className={`
                  sidebar-icon text-2xl mb-6 cursor-pointer transition-all duration-300 ease-in-out
                  ${active === icon.name
                  ? 'bg-white/20 backdrop-blur-md border border-white/30 p-2 rounded-xl scale-105 shadow-lg'
                  : 'opacity-70 hover:opacity-100 hover:scale-105'
                }
              `}
            >
              <img className='h-7 relative z-10' src={icon.src} alt={icon.name} />
            </div>
          ))}
        </aside>      
    </div>
  )
}

export default Home
