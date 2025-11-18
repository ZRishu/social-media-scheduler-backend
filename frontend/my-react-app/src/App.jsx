import Login from './components/Login'
import IntroductoryPage from './components/introductoryPage'
import PostCreation from './components/postCreation'
import './App.css'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'

const router = createBrowserRouter(
  [
    {
      path: '/',
      element: <IntroductoryPage/>
    },
    {
      path: '/signin',
      element: <Login/>
    },
    {
      path: '/postCreation',
      element: <PostCreation/>
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
