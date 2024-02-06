import './index.css'
import Header from '../Header'
import DashboardCard from '../DashboardCard'

const Dashboard = () => {

    const list = [
        {
            id: 1,
            imageUrl : "https://res.cloudinary.com/drvnhpatd/image/upload/v1706379166/vdlrgp7zuehwf8zlakau.jpg",
            title: 'Abstract_1',
            path: '/dashboard'
        },
        {
            id: 2,
            imageUrl : "https://res.cloudinary.com/drvnhpatd/image/upload/v1706379166/vdlrgp7zuehwf8zlakau.jpg",
            title: 'Abstract_2',
            path: '/view-status'
        },
        {
            id: 3,
            imageUrl : "https://res.cloudinary.com/drvnhpatd/image/upload/v1706379166/vdlrgp7zuehwf8zlakau.jpg",
            title: 'Abstract_3',
            path: '/notification'
        }
    ];
    const check_max = ()=>
    {
        if(list.length < 3)
        {
            return ( <div className='text-center'>
            <button className="btn btn-primary m-5">Upload</button>
            </div>
           );
        }
    }
    return (
       <div className="dashboard-container">
            
            <Header />
            <h1 className="heading">Submitted Abstracts</h1>
            <ul className = "dashboard-menu">
                {list.map((item) => (
                    <DashboardCard cardDetails={item} key={item.id} />
                ))}
            </ul>
            <br/>
            {check_max()}
       </div>
    );
}

export default Dashboard;