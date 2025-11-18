import React, { useState } from 'react';
import arrow from '../assets/arrow.png';
import IDverify from '../assets/IDverify.png';
import schedule from '../assets/schedule.png';
import media from '../assets/media.png';
import GirlOnPhone from '../assets/girlwithphone.png';
import GirlOnLaptop from '../assets/girlOnLaptop.png';
import "@fontsource/nunito-sans"
import "@fontsource/poppins"
import Pen from '../assets/pen.png';
import AIimage from '../assets/AIimage.png';
import Hashtag from '../assets/hashtag.png';
import X from '../assets/X.png';
import Youtube from '../assets/youtube.svg';
import { NavLink } from 'react-router-dom';
import DownArrow from '../assets/DownArrow.png';
import facebook from '../assets/Facebook.svg';
import twitter from '../assets/X.png';
import instagram from '../assets/instagram.svg';
import linkedIn from '../assets/linkedIn.png';
import Fmsg from '../assets/FMsg.svg';
import whatsapp from '../assets/whatsapp.svg';
import snapChat from '../assets/snapChat.svg';
import tiktok from '../assets/tiktok.svg';
import reddit from '../assets/reddit.svg';

const IntroductoryPage = () => {
  const [showFeaturesDropdown, setShowFeaturesDropdown] = useState(false);
  const [showSocialsDropdown, setShowSocialsDropdown] = useState(false);

  return (
    <div className='bg-blue'>
      <navbar className='flex justify-between p-3 bg-[#99D98C]'>
        <logo className='ml-4'>
            <img src="path/to/logo.png" alt="App Logo" />
        </logo>
        <div className='flex text-lg font-mono pt-1 gap-3 '>
            <div
              className='px-4 flex flex-row items-center justify-center hover:text-[#0A2133] border-sm p-1 bg-opacity-10 hover:bg-[#F3F9DC] transition-all ease-in-out duration-300 delay-75 rounded-full relative'
              onMouseEnter={() => setShowFeaturesDropdown(true)}
              onMouseLeave={() => setShowFeaturesDropdown(false)}
            >
              <a href="#">Feature</a>
              <img className='h-4 ml-1' src={DownArrow} alt="" />
              {showFeaturesDropdown && (
                <div className='absolute top-full left-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-20'>
                  <a href="#" className='block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100'>Scheduling</a>
                  <a href="#" className='block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100'>AI Post Generation</a>
                  <a href="#" className='block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100'>Analytics</a>
                </div>
              )}
            </div>
            <div
              className='px-4 flex flex-row items-center justify-center hover:text-[#0A2133] border-sm p-1 bg-opacity-10 hover:bg-[#F3F9DC] transition-all ease-in-out duration-300 delay-75 rounded-full relative'
              onMouseEnter={() => setShowSocialsDropdown(true)}
              onMouseLeave={() => setShowSocialsDropdown(false)}
            >
              <a href="@">Socials</a>
              <img className='h-4 ml-1' src={DownArrow} alt="" />
              {showSocialsDropdown && (
                <div className='absolute top-full left-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-20'>
                  <a href="#" className='block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100'>Facebook</a>
                  <a href="#" className='block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100'>Instagram</a>
                  <a href="#" className='block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100'>Twitter (X)</a>
                  <a href="#" className='block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100'>LinkedIn</a>
                  <a href="#" className='block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100'>YouTube</a>
                </div>
              )}
            </div>
        </div>
        <div className='gap-3 flex mr-8'>
            <NavLink to="/signin" className='bg-white rounded-2xl py-1 px-4 text-lg border-[.0625rem] relative shadow-sm hover:translate-y-[-2px] hover:shadow-lg transition-all border-black'>Log in</NavLink>
            <NavLink to="/signin" className='bg-[#52B69A] rounded-2xl relative shadow-sm hover:translate-y-[-2px] hover:shadow-lg transition-all py-1 px-4 text-lg border-[.0625rem] border-[#0A2133] border-spacing-2 '>Sign Up</NavLink>
        </div>
      </navbar>
      <header className='bg-background-img h-screen bg-cover bg-center'>
        <div className='flex flex-col text-center p-40 justify-center items-center'>
            <div className='relative z-10 flex flex-col text-center justify-center items-center'>
              <h1 className='font-sans text-6xl font-bold mb-6'>Welcome to Social Media Schedular</h1>
              <p className='text-xl mb-8'>where you can create Thoughts and Impresions of your in a new way and connect like never before.</p>
              <div className='border-[1px] border-[#0A2133] rounded-full p-[2px]'>
                <button className='bg-[#52B69A] text-white w-[190px] font-bold py-3 px-5 rounded-full text-lg hover:border-black hover:bg-gray-200 hover:text-black transition duration-300 flex items-center justify-center text-center gap-2'>Get Started <img className='h-8' src={arrow} alt="" /></button>
              </div>
            </div>
            <div className='Left w-full mt-[-100px] pb-5 flex justify-between'>
              <img className='h-[50px] border-grey-500 m-1 p-[0.5px] opacity-70 z-10  border-[0.75px] rounded-md absolute right-[50px]  hover:scale-90 duration-300 transition-all' src={facebook} loading='lazy' alt="" />
              <img className='h-[45px] border-grey-500 m-1 p-[0.5px] opacity-70 z-10  border-[0.75px] rounded-md absolute left-[50px] hover:scale-90 duration-300 transition-all' src={instagram} loading='lazy' alt="" />
              <img className='h-[45px] border-grey-500 m-1 p-[0.5px] opacity-70 z-20  border-[0.75px] rounded-md absolute left-[90px] bottom-[50px]  hover:scale-90 duration-300 transition-all' src={twitter} loading='lazy' alt="" />
              <img className='h-[55px] border-grey-500 m-1 p-[0.5px] opacity-70 z-40  border-[0.75px] rounded-md absolute left-[250px] bottom-[200px]  hover:scale-90 duration-300 transition-all' src={Youtube} loading='lazy' alt="" />
              <img className='h-[60px] border-grey-500 m-1 p-[0.5px] opacity-70 z-20  border-[0.75px] rounded-md absolute right-[150px] bottom-[60px] hover:scale-90 duration-300 transition-all' src={linkedIn} loading='lazy' alt="" />
              <img className='h-[45px] border-grey-500 m-1 p-[0.5px] blur-sm opacity-70 z-[-10px]  border-[0.75px] rounded-md absolute right-[520px] bottom-[100px] hover:scale-90 duration-300 transition-all ' src={Fmsg} loading='lazy' alt="" />
              <img className='h-[45px] border-grey-500 m-1 p-[0.5px] opacity-70 z-[-10px]  border-[0.75px] rounded-md absolute right-[120px] top-[180px] hover:scale-90 duration-300 transition-all ' src={whatsapp} loading='lazy' alt="" />
              <img className='h-[45px] border-grey-500 m-1 p-[0.5px] blur-sm opacity-70 z-[-10px]  border-[0.75px] rounded-md absolute left-[160px] top-[160px] hover:scale-90 duration-300 transition-all ' src={snapChat} loading='lazy' alt="" />
              <img className='h-[45px] border-grey-500 m-1 p-[0.5px] opacity-70 blur-sm z-[-10px]  border-[0.75px] rounded-md absolute right-[280px] bottom-[300px] hover:scale-90 duration-300 transition-all ' src={tiktok} loading='lazy' alt="" />
              <img className='h-[45px] border-grey-500 m-1 p-[0.5px] blur-sm opacity-70 z-[-10px]  border-[0.75px] rounded-md absolute left-[530px] bottom-[100px] hover:scale-90 duration-300 transition-all ' src={reddit} loading='lazy' alt="" />
            </div>
        </div>
      </header>
      {/* subtext1 */}
      <sub>
        <div className='w-[85%] h-[600px] bg-gray-200 mx-auto my-20 flex rounded-2xl'>
          <div className='flex justify-center items-center leftDIv rounded-l-2xl'>
            <img className='h-[500px]' src={GirlOnPhone} alt="GirlWithPhone" />
          </div>
          <div className='rightDiv w-[50%] p-10'>
            <h1 className='text-5xl font-bold  '>Schedule your Socials for Future</h1>
            <p className='text-lg mt-8 w-[80%] font-sans'>Easily plan and schedule your social media Socials ahead of time with our intuitive scheduling tools. Save time and maintain a consistent online presence by automating your Socialing process.</p>
            <ul>
              <li className='flex gap-3 mt-8 items-center'>
                <img className='h-10' src={IDverify} alt="" />
                <p className='text-[15px] hover:scale-105 transition-all delay-100 opacity-80 hover:opacity-100'>connect with your Ids</p>
              </li>
              <li className='flex gap-4 mt-8 items-center'>
                <img className='h-10' src={schedule} alt="" />
                <p className='text-[15px] hover:scale-105 transition-all delay-100 opacity-80 hover:opacity-100'>Schedule your Socials</p>
              </li>
              <li className='flex gap-4 mt-8 items-center'>
                <img className='h-10' src={media} alt="" />
                <p className='text-[15px] hover:scale-105 transition-all delay-100 opacity-80 hover:opacity-100'>add media to Social</p>
              </li>
            </ul>
          </div>
        </div>
      </sub>
      {/* subtext2 */}
      <sub>
        <div className='w-[85%] h-[600px] bg-gray-200 mx-auto my-20 flex rounded-2xl'>
          <div className='rightDiv w-[50%] p-12'>
            <h1 className='text-5xl font-bold  '>Write with AI powered Self-Generating post</h1>
            <p className='text-lg mt-8 w-[80%] font-sans'>Generate AI-powered posts as you write. Our AI tracks your content, helping to develop ideas, creating relevant images, and even suggesting perfect hashtags to complement your text.</p>
            <ul>
              <li className='flex gap-3 mt-8 items-center'>
                <img className='h-9' src={Pen} alt="pen" />
                <p className='text-[15px] hover:scale-105 transition-all delay-100 opacity-80 hover:opacity-100'>AI can help Generate a perfect-post</p>
              </li>
              <li className='flex gap-4 mt-8 items-center'>
                <img className='h-9' src={AIimage} alt="image" />
                <p className='text-[15px] hover:scale-105 transition-all delay-100 opacity-80 hover:opacity-100'>Generate relevent AI images</p>
              </li>
              <li className='flex gap-4 mt-8 items-center'>
                <img className='h-10' src={Hashtag} alt="hashtag" />
                <p className='text-[15px] hover:scale-105 transition-all delay-100 opacity-80 hover:opacity-100'>Add Hashtags using AI</p>
              </li>
            </ul>
          </div>
          <div className='flex justify-center items-center leftDIv rounded-l-2xl'>
            <img className='h-[500px] pr-4' src={GirlOnLaptop} alt="GirlWithPhone" />
          </div>
        </div>
      </sub>
      {/* card */}
      <div className='w-[85%] h-[150px] bg-gray-200 mx-auto my-20 flex rounded-2xl'>
        <div className='w-[30%] '>
          <h3 className='m-8 text-xl font-semibold'>
            Social Schedular can connect with multiple social media platforms including..
          </h3>
        </div>

        <div className='flex justify-between items-center gap-10'>
            <img className='h-24' src="https://www.freepnglogos.com/uploads/facebook-logo-13.png" alt="Facebook" />
            <img className='h-28' src="https://www.freepnglogos.com/uploads/instagram-logos-png-images-free-download-2.png" alt="Instagram" />
            <img className='h-24' src="https://www.freepnglogos.com/uploads/linkedin-logo-design-30.png" alt="LinkedIn" />
            <img className='h-28' src={X} alt="X" />
            <img className='h-32' src={Youtube} alt="Youtube" />
        </div>
      </div>
      {/* footer */}
      <footer>
        <div className='bg-[#99D98C] text-center p-4'>
            <p className='text-lg'>© 2025 Social Schedular. All rights reserved.</p>
        </div>
      </footer>
    </div>
  )
}

export default IntroductoryPage;