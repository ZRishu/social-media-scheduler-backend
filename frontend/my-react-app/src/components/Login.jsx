import React, { useState } from 'react';
import LinkedIn from '../assets/LinkedIn.png';
import Eyeopen from '../assets/eyeopen.png';
import Insta from '../assets/instagram.png';
import faceBook from '../assets/facebook.png';
import X from '../assets/X.png';
import { NavLink,Link } from 'react-router-dom';


const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [rememberMe, setRememberMe] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleLogin = (e) => {
    e.preventDefault();
    console.log('Logging in with:', { email, password, rememberMe });
    alert('Login functionality not implemented in this demo.');
  };

  return (
    <div className="min-h-screen flex font-sans text-gray-800 ">
      <div className="flex-1 flex flex-col justify-center items-center bg-white opactity-5 text-center">
        {/* <div className="text-5xl font-bold text-[#184E77] mb-5 self-center pr-20 ml-20">
          Post schedular
        </div> */}
        <h2 className="text-4xl font-bold mb-8 mr-36 ml-36">Login</h2>

        <div className='grid grid-cols-2 gap-x-8'>
          <button className="flex items-center justify-center w-[200px] h-[65px] py-3 px-3 border border-gray-300 rounded-lg bg-white cursor-pointer text-sm mb-6 hover:bg-gray-50 transition-colors duration-300">
          Sign in with 
            <p className='font-bold pl-1'></p>
            <img src={LinkedIn} alt="linkedin logo" className="h-7 mr-3" />
          </button>
          <button className="flex items-center justify-center w-[200px] h-[65px] py-3 px-3 border border-gray-300 rounded-lg bg-white cursor-pointer text-sm mb-6 hover:bg-gray-50 transition-colors duration-300">
          Sign in with 
            <p className='font-bold pl-1'></p>
            <img src={Insta} alt="linkedin logo" className="h-8 mr-3" />
          </button>
          <button className="flex items-center justify-center w-[200px] h-[65px] py-3 px-3 border border-gray-300 rounded-lg bg-white cursor-pointer text-sm mb-6 hover:bg-gray-50 transition-colors duration-300">
          Sign in with 
            <p className='font-bold pl-1'></p>
            <img src={faceBook} alt="facebook logo" className="h-9 mr-3" />
          </button>
          <button className="flex items-center justify-center w-[200px] h-[65px] py-3 px-3 border border-gray-300 rounded-lg bg-white cursor-pointer text-sm mb-6 hover:bg-gray-50 transition-colors duration-300">
          Sign in with 
            <p className='font-bold pl-1'></p>
            <img src={X} alt="X logo" className="h-8 mr-3" />
          </button>
        </div>

        <div className="relative w-3/4 text-center text-gray-500 before:content-[''] before:absolute before:top-1/2 before:left-0 before:w-[190px] before:h-px before:bg-gray-200 after:content-[''] after:absolute after:top-1/2 after:right-0 after:w-2/6 after:h-px after:bg-gray-200">
          Or sign in with id
        </div>

        <form onSubmit={handleLogin} className="w-3/5">
          <div className="mb-5 text-left">
            <label htmlFor="email" className="block mb-2 font-bold text-sm">
              Email ID
            </label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Email ID"
              required
              className="w-full p-3 border border-gray-300 rounded-lg text-lg focus:outline-none transition-all focus:ring-2 focus:ring-[#168AAD]"
            />
          </div>

          <div className="mb-4 text-left relative">
            <label htmlFor="password" className="block mb-2 font-bold text-sm">
              Password
            </label>
            <input
              type={showPassword ? 'text' : 'password'}
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Password"
              required
              className="w-full p-3 border border-gray-300 rounded-lg text-lg focus:outline-none transition-all focus:ring-2 focus:ring-[#168AAD]"
            />
            <span
              className="absolute right-4 top-1/2 mt-3 transform -translate-y-1/2 cursor-pointer text-gray-500"
              onClick={() => setShowPassword(!showPassword)}
            >
              {showPassword ? '🙈' : '👀'}
            </span>
          </div>

          <div className="flex justify-between items-center mb-8 text-sm">
            <label className="flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={rememberMe}
                onChange={(e) => setRememberMe(e.target.checked)}
                className="mr-2 accent-purple-600"
              />
              Keep me logged in
            </label>
            <a href="#" className="text-[#184E77] font-semibold hover:underline">
              Forgot password?
            </a>
          </div>

          <Link to="/postCreation"
            type="submit"
            className="w-full py-4 border-none inline-block rounded-lg bg-gradient-to-r from-[#184E77] to-[#168AAD] text-white text-xl font-bold cursor-pointer mb-8 hover:opacity-90 transition-opacity duration-300"
          >
            Login
          </Link>
        </form>

        <div className="text-sm text-gray-600">
          Don't have an account?{' '}
          <a href="#" className="text-[#184E77] font-bold hover:underline">
            Sign up
          </a>
        </div>
      </div>

      <div className="flex-1 bg-gradient-to-br from-[#D9ED92] to-[#34A0A4] flex justify-center items-center relative overflow-hidden">
        <h1 className="quote text-6xl text-gray-800 text-center leading-tight font-bold z-10 max-w-lg">
          "Changing the way the world writes"
        </h1>

        {/* Abstract Shapes - using absolute positioning and background colors for similar effect */}
        {/* Shape 1: Orange-ish circle */}
        <div className="absolute w-64 h-64 bg-orange-400 rounded-full opacity-70 blur-xl top-[-50px] right-[-50px] transform rotate-30"></div>
        {/* Shape 2: Light purple circle */}
        <div className="absolute w-72 h-72 bg-[#168AAD] rounded-full opacity-70 blur-xl bottom-[-80px] left-[-80px] transform -rotate-45"></div>
        {/* Shape 3: Light blue circle */}
        <div className="absolute w-40 h-40 bg-blue-300 rounded-full opacity-60 blur-xl top-1/3 left-1/4 transform rotate-15"></div>
        {/* Shape 4: Pinkish half circle (simulated with rounded corners) */}
        <div className="absolute w-48 h-24 bg-pink-300 rounded-r-full opacity-70 blur-xl bottom-1/4 right-1/4 transform -rotate-20"></div>
      </div>
    </div>
  );
};

export default Login;