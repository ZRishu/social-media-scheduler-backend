import Login from './components/Login'
import IntroductoryPage from './components/introductoryPage'
import PostCreation from './components/postCreation'
import Drafts from './components/drafts'
import Ransome from './components/sideBar'
import Home from './components/home'

import './App.css'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'

const router = createBrowserRouter(
  [
    {
      path: '/',
      element: <Home/>
    },
    {
      path: '/signin',
      element: <Login/>
    },
    {
      path: '/postCreation',
      element: <PostCreation/>
    },
    {
      path: '/drafts',
      element: <Drafts/>
    },
    {
      path: '/ransome',
      element:<Ransome/>
    }
  ]
) 
function App() {

  return (
    <>
      <RouterProvider router={router}/>
      
    </>
  )
}

export default App
