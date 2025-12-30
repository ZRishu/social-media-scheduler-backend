import React from 'react'
import Ransome from './sideBar'
import Navbar from './subcomponent/navbar';
import Draftcard from './subcomponent/draftCard';
import User from '../assets/user.png';

const home = () => {
  return (
    <div>
        <div className="flex h-screen bg-gray-100 font-sans antialiased">
            <div className="flex flex-1 bg-white shadow-xl overflow-hidden rounded-none">
                {/* sidebar */}
                <Ransome/>
                {/* main content area */}
                <div className="flex-1 flex flex-col overflow-hidden items-center">
                    <div className='w-[460px]'>
                        <div className='profileContent flex flex-row items-center p-4 h-[50px] gap-2 border border-gray-600'>
                            <div className='Profile'>
                                <img src="" alt="P" />
                            </div>
                            <div className='idName text-[14px] '>
                                <h2 className='text-bold '>John Doe</h2>
                                <p className=' text-[12px]'>@johndoe</p>
                            </div>
                            <div className='hidden'>
                                <a href="#">
                                    Follow
                                </a>
                            </div>
                            <div className='option ml-auto text-bold '>
                                ...
                            </div>
                        </div>
                        <div className='ImageSection h-[615px] p-[0.8px] rounded-[5px] border border-gray-600 '>
                            <img src="" alt="POST" />
                        </div>
                        <div className='flex flex-row items-center gap-2 px-2'>
                            <div className='LikeSection gap-1'>
                                <div className='Like'>

                                </div>
                                <div className='LikeCount'>
                                    1.5M
                                </div>
                            </div>
                            <div className='CommentSection'>
                                <div className='Comment'>

                                </div>
                                <div className='CommentCount'>
                                    500K            
                                </div>
                            </div>
                            <div className='ShareSection'>
                                <div className='Share'>

                                </div>
                                <div className='ShareCount'>

                                </div>
                            </div>
                        </div>
                        <div className='Caption align-middle px-2 text-sm'>
                            Lorem ipsum dolor sit amet consectetur adipisicing.
                        </div>
                    </div>
                </div>      
            </div>
        </div>        
    </div>
  )
}

export default home
