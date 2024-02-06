import "./index.css";
import Header from "../Header";
import { Link } from "react-router-dom";
const Home = () => {
  return (
    <>
      <Header />
      <div className="home-container">
        <div className="home-content">
          <h1 className="home-heading"> Welcome Speaker </h1>
          <img
            src="https://assets.ccbp.in/frontend/react-js/nxt-trendz-home-img.png"
            alt="clothes that get you noticed"
            className="home-mobile-img"
          />
          <p className="home-description">
            Welcome to our conference website! Join industry leaders, academics,
            and enthusiasts for a dynamic exchange of ideas, insights, and
            innovations. Explore cutting-edge research, engage in lively
            discussions, and network with professionals from around the globe.
          </p>
          <Link to="/upload-paper">
            <button type="button" className="shop-now-button">
              Upload Paper
            </button>
          </Link>
        </div>
        <img
          src="https://res.cloudinary.com/drvnhpatd/image/upload/v1706455644/sgllcp3jkrz5ibku3qof.jpg"
          alt="clothes that get you noticed"
          className="home-desktop-img"
        />
      </div>
    </>
  );
};

export default Home;
