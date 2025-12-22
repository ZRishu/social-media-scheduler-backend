import React, { useState } from 'react'
import { Link } from 'react-router-dom';


const postAreaConfig = [
    { name: 'Create Post' , direct: '/postCreation'},
    { name: 'Drafts' , direct: '/drafts'},
    { name: 'Scheduled Posts' },
    { name: 'Past Posts' },
  ];

export default function Navbar({ active: propActive, setActive: propSetActive }) {
  const [localActive, setLocalActive] = useState(propActive || 'Create Post' );

  const active = propActive !== undefined ? propActive : localActive;
  const setActive = propSetActive || setLocalActive;

  return (
    <nav className="flex space-x-6 p-1">
      {postAreaConfig.map((item) => (
        <Link
          to={item.direct ? item.direct : '#'}
          key={item.name}
          onClick={() => setActive(item.name)}
          className={`relative px-0 text-sm font-medium ${active === item.name
              ? 'text-[#34A0A4] font-semibold pb-2 after:absolute after:bottom-0 after:left-0 after:w-full after:h-[1.8px] after:bg-[#34A0A4]'
              : 'text-gray-600 hover:text-[#34A0A4] transition-colors pb-2'
            }`}
        >
          {item.name}
        </Link>
      ))}
    </nav>
  );
}
