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

     const toDateTimeFormat = (date) => {
        const dateTime = new Date(date);
        const year = dateTime.getFullYear();
        const month = dateTime.getMonth() + 1;
        const day = dateTime.getDate();
        const hours = dateTime.getHours();
        const minutes = dateTime.getMinutes();
        const seconds = dateTime.getSeconds();
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
     };

    const apiResponseStatusConstants = {
        intial: 'INITIAL',
        sucess: 'SUCCESS',
        failure: 'FAILURE',
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
            if(response.ok) {
                const data = await response.json();
                console.log(data);
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
            } 
            catch (error) {
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
                <div>
                    <table className='table-container'>
                        <tr>
                            <th>Title</th>
                            <th>Status</th>
                            <th>Created At</th>
                        </tr>
                        {apiResponse.sessions.map(session => (
                             <tr key={session.id}>
                                <td>{session.title}</td>
                                <td>Pending</td>
                                <td>{toDateTimeFormat(session.date)}</td>
                            </tr>
                        ))}
                    </table>
                </div>
            </div>
        );
    }

    const renderViewStatusFailureView = () => <ViewApiFailureView/>;



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