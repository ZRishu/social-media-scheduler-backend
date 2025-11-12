import React from 'react'
import arrow from '../assets/arrow.png';
import backgroundImg from '../assets/backgroundImg.png';
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
import Youtube from '../assets/youtube.png';
import { NavLink } from 'react-router-dom';
const introductoryPage = () => {
  return (
    <div className='bg-blue'>
      <navbar className='flex justify-between p-3 bg-[#99D98C]'>
        <logo>
            <img src="path/to/logo.png" alt="App Logo" />
        </logo>
        <div className='flex text-lg font-mono pt-1 '>
            <div className='px-4'><a href="#">Feature</a></div>
            <div className='px-4'><a href="@">Socials</a></div>
        </div>
        <div className='gap-3 flex'>
            <NavLink to="/signin" className='bg-white rounded-2xl border-spacing-1 py-1 px-4 text-lg'>Log in</NavLink>
            <NavLink to="/signin" className='bg-[#52B69A] rounded-2xl border-spacing-1 py-1 px-4 text-lg'>Sign Up</NavLink>
        </div>
      </navbar>
      <header className='bg-background-img h-screen bg-cover bg-center'>
        <div className='flex flex-col text-center p-40 justify-center items-center'>
            <h1 className='font-sans text-5xl font-bold mb-6'>Welcome to Social Schedular</h1>
            <p className='text-xl mb-8'>where you can create Thoughts and Impresions of your in a new way and connect like never before.</p>
            <button className='bg-[#52B69A] text-white w-[190px] font-bold py-3 px-5 rounded-full text-lg hover:border-black hover:bg-gray-200 hover:text-black transition duration-300 flex items-center justify-center text-center gap-2'>Get Started <img className='h-8' src={arrow} alt="" /></button>
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
                <p className='text-[15px]'>connect with your Ids</p>
              </li>
              <li className='flex gap-4 mt-8 items-center'>
                <img className='h-10' src={schedule} alt="" />
                <p className='text-[15px]'>Schedule your Socials</p>
              </li>
              <li className='flex gap-4 mt-8 items-center'>
                <img className='h-10' src={media} alt="" />
                <p className='text-[15px]'>add media to Social</p>
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
                <p className='text-[15px]'>AI can help Generate a perfect-post</p>
              </li>
              <li className='flex gap-4 mt-8 items-center'>
                <img className='h-9' src={AIimage} alt="image" />
                <p className='text-[15px]'>Generate relevent AI images</p>
              </li>
              <li className='flex gap-4 mt-8 items-center'>
                <img className='h-10' src={Hashtag} alt="hashtag" />
                <p className='text-[15px]'>Add Hashtags using AI</p>
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
            <img className='h-32' src={X} alt="X" />
            <img className='h-24' src="https://www.freepnglogos.com/uploads/facebook-logo-13.png" alt="Facebook" />
            <img className='h-28' src="https://www.freepnglogos.com/uploads/instagram-logos-png-images-free-download-2.png" alt="Instagram" />
            <img className='h-24' src="https://www.freepnglogos.com/uploads/linkedin-logo-design-30.png" alt="LinkedIn" />
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

export default introductoryPage
