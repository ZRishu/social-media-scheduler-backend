import React, { useState, useRef } from 'react';
import Facebook from '../assets/facebook.png';
import Twitter from '../assets/X.png';
import LinkedIn from '../assets/linkedIn.png';
import Instagram from '../assets/instagram.png';
import YouTube from '../assets/youtube.png';
import Create from '../assets/create.png';
import Home from '../assets/home.png';
import Notification from '../assets/notification.png';
import Setting from '../assets/setting.png';
import Calendar from '../assets/calendar.png';
import Add from '../assets/add.png';
import AiImage from '../assets/AI image.png';
import Music from '../assets/music.png';
import User from '../assets/user.png';
import { Link } from 'react-router-dom';
import Navbar from './subcomponent/navbar';
import Draftcard from './subcomponent/draftCard';

function App() {
  const [active, setActive] = useState('Create');
  const [showAddFilesPopup, setShowAddFilesPopup] = useState(false);

  const anyFileRef = useRef(null);
  const imageFileRef = useRef(null);
  const videoFileRef = useRef(null);
  const audioFileRef = useRef(null);

  const icons = [
    { name: 'Home', src: Home },
    { name: 'Create', src: Create },
    { name: 'Notification', src: Notification },
    { name: 'Calendar', src: Calendar },
    { name: 'Setting', src: Setting },
  ];

  const postPlatforms = [
    { name: 'Facebook', src: Facebook },
    { name: 'Twitter', src: Twitter },
    { name: 'LinkedIn', src: LinkedIn },
    { name: 'Instagram', src: Instagram },
    { name: 'YouTube', src: YouTube },
  ]

  const postGneration = [
    { name: 'AI Post Suggestions' },
    { name: 'AI Image Suggestions', src: AiImage },
  ];

  const postAreaConfig = [
    { name: 'Create Post' , direct: '/postCreation'},
    { name: 'Drafts', dirct:'/draft' },
    { name: 'Scheduled Posts' },
    { name: 'Past Posts' },
  ];

  const handleAddFiles = () => {
    if (anyFileRef.current) anyFileRef.current.click();
    setShowAddFilesPopup(false);
  };
  const handleAddImage = () => {
    if (imageFileRef.current) imageFileRef.current.click();
    setShowAddFilesPopup(false);
  };
  const handleAddAudio = () => {
    if (audioFileRef.current) audioFileRef.current.click();
    setShowAddFilesPopup(false);
  }
  const handleAddVideo = () => {
    if (videoFileRef.current) videoFileRef.current.click();
    setShowAddFilesPopup(false);
  };

  const onAnyFilesSelected = (e) => {
    const files = Array.from(e.target.files || []);
    if (files.length) {
      console.log('Selected files:', files);
    }
    e.target.value = '';
  };
  const onImageSelected = (e) => {
    const files = Array.from(e.target.files || []);
    if (files.length) {
      console.log('Selected images:', files);
    }
    e.target.value = '';
  };
  const onAudioSelected = (e) => {
    const files = Array.from(e.target.files || []);
    if (files.length) {
      console.log('Selected images:', files);
    }
    e.target.value = '';
  };
  const onVideoSelected = (e) => {
    const files = Array.from(e.target.files || []);
    if (files.length) {
      console.log('Selected videos:', files);
    }
    e.target.value = '';
  };

  return (
    <div className="flex h-screen bg-gray-100 font-sans antialiased">
      <div className="flex flex-1 w-full max-w-full mx-auto bg-white shadow-xl overflow-hidden rounded-none">
        {/* sidebar */}
        <aside className="w-16 bg-gradient-to-br from-[#76C893] to-[#1E6091] text-white flex flex-col items-center py-4 shadow-md">
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

        <main className="flex-1 flex flex-col">
          <header className="flex justify-between items-center px-6 py-2 border-b border-gray-200 bg-white shadow-sm">
            {/* navbar */}
            <Navbar/>
            <div className="flex items-center space-x-3">
              <button className="px-4 py-2 bg-[#34A0A4] text-white rounded-lg text-sm font-medium hover:bg-[#184E77] transition-colors shadow-md">Calendar Schedule</button>
              <button className="p-1 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors">
                <img className='h-7' src={User} alt="user" />
              </button>
            </div>
          </header>
              <section className="flex flex-1 p-6 bg-gradient-to-br from-indigo-50 via-purple-50 to-pink-50">
                <Draftcard/>
              </section>
        </main>
      </div>
    </div>
  );
}

export default App;
