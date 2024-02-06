import "./index.css";
import { Link } from "react-router-dom";

const DashboardCard = (props) => {
  const { cardDetails } = props;
  const { id, imageUrl, title } = cardDetails;
  return (
    <Link className = "dashboard-item" to={`/dashboard/${id}`}>
      <li className="card">
        <img src={imageUrl} alt={title} className="card-image" />
        <div className="card-content">
          <h2 className="card-title">{title}</h2>
          {/* <p className="card-description">{description}</p> */}
        </div>
      </li>
    </Link>
  );
};

export default DashboardCard;
