import React from 'react'
import Announcement from '../components/Announcements.jsx'
import Navbar from '../components/Navbar.jsx'
import Slider from "../components/Slider.jsx"
import Categories from '../components/Categories.jsx'
import Products from '../components/Products.jsx'
import Footer from '../components/Footer.jsx'
import Newsletter from '../components/Newsletter.jsx'


const Home = () => {
  return (
    <div>
      <Announcement/>
      <Navbar/>
      <Slider/>
      <Categories/>
      <Products/>
      <Newsletter/>
      <Footer/>  
    </div>
  )
}

export default Home
