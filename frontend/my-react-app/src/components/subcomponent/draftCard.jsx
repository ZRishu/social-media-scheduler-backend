import React from 'react'

function draftCard() {
  return (
    <div>
      <div className='w-[400px] bg-white rounded-md shadow-md overflow-hidden'>
        <div className='h-48 bg-gray-300'>
            <img src="" alt="" className='w-full h-full object-cover' />
        </div>
        <div className='p-4 rounded-b-md bg-blue-500 text-white'>
            <h3 className='font-bold text-lg mb-2'>HEAD</h3>
            <p className='text-sm leading-relaxed'>Lorem, ipsum dolor sit amet consectetur adipisicing elit. Veniam quo magni facilis ad sint molestiae! Lorem ipsum, dolor sit amet consectetur adipisicing elit. Dolorum, deleniti?</p>
        </div>
      </div>
    </div>
  )
}

export default draftCard
