import "./index.css";
import { Link } from "react-router-dom";
import { MdDelete } from "react-icons/md";
import { useState } from "react";
import { Button, Modal } from 'react-bootstrap';
import { useHistory } from "react-router-dom";

const DashboardCard = (props) => {
  const {id, cardDetails, onDeleteSession } = props;
  const {  title } = cardDetails;
  const imageUrl = "https://res.cloudinary.com/drvnhpatd/image/upload/v1706379166/vdlrgp7zuehwf8zlakau.jpg";
  const history = useHistory();
   

  const [show, setShow] = useState(false);

  const handleDelete = () => {
    console.log("Delete triggered");
    onDeleteSession(id);
    handleClose();
  };

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const onClickNavigate = () => {
    history.push(`/dashboard/${id}`);
  }

  const renderDeleteConfirmationModal = () => {
    return (
      <>
        {/* <Button variant="danger" onClick={handleShow}>
          Delete
        </Button> */}
  
        <Modal show={show} onHide={handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Confirm Delete</Modal.Title>
          </Modal.Header>
          <Modal.Body>Are you sure you want to delete?</Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
              Cancel
            </Button>
            <Button variant="primary" onClick={handleDelete}>
              Delete
            </Button>
          </Modal.Footer>
        </Modal>
      </>
    );
  }
  return (
      <>
        <li className="card">
          <img src={imageUrl} alt={title} onClick = {onClickNavigate} className="card-image" />
          <div className="card-content">
            <h2 onClick = {onClickNavigate} className="card-title">{title}</h2>
            {/* <p className="card-description">{description}</p> */}
            <MdDelete className="dashboard-card-delete-btn" onClick={handleShow}/>
          </div>
        </li>
        {renderDeleteConfirmationModal()}
      </>
  );
};

export default DashboardCard;
