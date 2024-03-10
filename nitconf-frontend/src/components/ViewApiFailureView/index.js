import './index.css';


const ViewApiFailureView = (props) => {
    return (
        <div className="view-status-failure-view">
            <img className='failed-image' src = "https://res.cloudinary.com/drvnhpatd/image/upload/v1707235895/xszezp5vdsxkdyxsgppz.jpg" alt = "no-response-image" />
            <h1 className='failed-view-text'>Failed to fetch data</h1>
        </div>
    );
}

export default ViewApiFailureView;