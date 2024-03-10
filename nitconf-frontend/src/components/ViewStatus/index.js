import './index.css';
import Header from '../Header';
import ViewStatusCard from '../ViewStatusCard';
import { useState , useEffect} from 'react';
import Cookies from 'js-cookie';
// import react-loader-spinner
import Loader from 'react-loader-spinner';
import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";
import ViewApiFailureView from '../ViewApiFailureView';


const ViewStatus = () => {

    // const sessions = [
    //     {
    //         id: 1,
    //         name: 'Session 1',
    //         status: 'PENDING',
    //         lastModified: new Date("2021-09-01T10:00:00Z"),
    //     },
    //     {
    //         id: 2,
    //         name: 'Session 2',
    //         status: 'ACTIVE',
    //         lastModified: new Date("2021-09-01T11:00:00Z"),
    //     }
    // ];

    const apiResponseStatusConstants = {
        intial: 'INITIAL',
        sucess: 'SUCCESS',
        failure: 'FAILURE',
    }

    const headerDetails = {
        id : 1,
        name: 'Title',
        status: 'Status',
        dateCreated: 'Created At'
    }

    const [apiResponse, setApiResponse] = useState({
        apiResponseStatus : null,
        sessions: null,
        errorMsg : null
    });

    useEffect(() => {
        const getSessions = async () => {
            setApiResponse(prevApiResponse => ({
                ...prevApiResponse,
                apiResponseStatus: apiResponseStatusConstants.intial
            }));
            try {
                const jwtToken = Cookies.get('jwt_token');
            const url = 'http://localhost:8082/api/abstract';
            const options = {
                method: 'GET',
                headers: {
                "Authorization": `Bearer ${jwtToken}`,
                }
            }
            const response = await fetch(url, options);
            const data = await response.json();
            console.log(data);
            if(response.ok) {
                alert('Sessions fetched successfully');
                setApiResponse({
                    apiResponseStatus: apiResponseStatusConstants.sucess,
                    sessions: data,
                    errorMsg: null
                });                
            }
            else {
                setApiResponse({
                    apiResponseStatus: apiResponseStatusConstants.failure,
                    sessions: null,
                    errorMsg: 'Error fetching sessions'
                });
                alert('Error fetching sessions');
                return;
            }
            } catch (error) {
                setApiResponse({
                    apiResponseStatus: apiResponseStatusConstants.failure,
                    sessions: null,
                    errorMsg: `Error fetching sessions: ${error.message}`
                });
            }
        }
        getSessions();
    }, []);


    const renderViewStatusLoadingView = () => {
        return (
            <div className="loader-container">
                <Loader type="ThreeDots" color="#00BFFF" height={80} width={80} />
            </div>
        );
    }

    const renderViewStatusSuccessView = () => {
        return (
            <div className="view-status-container">
                <h1 className="view-status-main-heading text-center m-5">View Status</h1>
                <ul className="view-status-menu">
                    <li className="view-status-menu-item" key={headerDetails.id}>
                        <p className='view-status-titleH'>{headerDetails.name}</p>
                        <p className='view-status-statusH'>{headerDetails.status}</p>
                        <p className='view-status-last-modifiedH'>{headerDetails.dateCreated.toLocaleString()}</p>
                    </li>
                    {
                        apiResponse.sessions.map((session) => <ViewStatusCard id = {session.id} sessionDetails = {session}/>)
                    }
                </ul>    
            </div>
        );
    }

    const renderViewStatusFailureView = () => {
        return (<ViewApiFailureView/>);
    }



    const renderViewStatus = () => {
        const {apiResponseStatus} = apiResponse;
        switch (apiResponseStatus) {
            case apiResponseStatusConstants.intial:
                return renderViewStatusLoadingView();
            case apiResponseStatusConstants.sucess:
                return renderViewStatusSuccessView();
            case apiResponseStatusConstants.failure:
                return renderViewStatusFailureView();
            default:
                return null;
        }
    }
    return (
        <>
            <Header/>
            {renderViewStatus()}
        </>
    );
}

export default ViewStatus;