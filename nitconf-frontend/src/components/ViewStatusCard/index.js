import './index.css';

const ViewStatusCard = (props) => {
    const {id} = props;
    const {sessionDetails} = props;
    const {title, status, date} = sessionDetails;

    return (
        <li className="view-status-menu-item" key={id}>
            <p className='view-status-title'>{title}</p>
            <p className='view-status-status'>{status}</p>
            <p className='view-status-last-modified'>{date.toLocaleString()}</p>
        </li>
    );

}

export default ViewStatusCard;