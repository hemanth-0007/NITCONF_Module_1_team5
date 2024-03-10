import "./index.css";
import { useParams } from "react-router-dom";
// import { BsDashSquare, BsPlusSquare } from "react-icons/bs";
import { useEffect, useState } from "react";
import Cookies from "js-cookie";
import Loader from "react-loader-spinner";
import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";
import ViewApiFailureView from "../ViewApiFailureView";
import ModifyPaper from "../ModifyPaper";
import { Button } from "react-bootstrap";

const PaperDetailedView = (props) => {
  const apiStatusConstants = {
    initial: "INITIAL",
    progess: "PROGRESS",
    success: "SUCCESS",
    failure: "FAILURE",
  };

  const [pdfString, setPdfString] = useState("");
  // get the id from the url path params
  // const paperId = props.match.params.id;

  const { paperId } = useParams();
  //console.log(paperId);

  // const title = "Sample Title";
  // const description =
  //   "This is a sample description. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam convallis purus nec turpis facilisis, nec vestibulum justo luctus.";
  const deadline = "January 31, 2024";
  const numReviewers = 3;
  // const version = 1.1;
  // const comments = [
  //   "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
  //   "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.",
  //   "Donec ac lectus in libero pharetra fermentum.",
  // ];
  // const tags = ["tag1", "tag2", "tag3"];

  const [apiResponse, setApiResponse] = useState({
    apiResponseStatus: apiStatusConstants.initial,
    data: {},
  });

  const [isModifyPaper, setisModifyPaper] = useState(false);

  const fetchPdf = async () => {
    try {
      const jwtToken = Cookies.get("jwt_token");
      console.log(paperId);
      const url = `http://localhost:8082/api/abstract/doc/${paperId}`;
      const options = {
        method: "GET",
        headers: {
          "Content-Type": "application/pdf",
          Authorization: `Bearer ${jwtToken}`,
        },
      };
      const response = await fetch(url, options);
      console.log(response);
      if (response.ok) {
        console.log("PDF fetched successfully");
        alert("PDF fetched successfully");
        const blob = await response.blob();
        console.log(blob);
        const pdfUrl = URL.createObjectURL(blob);
        console.log(pdfUrl);
        setPdfString(pdfUrl);
        
        // window.open(pdfUrl, '_blank');
        // let base64String;

        // let reader = new FileReader();
        // reader.readAsDataURL(blob);
        // reader.onloadend = () => {
        //   base64String = reader.result;
        //   setPdfString(base64String.substr(base64String.indexOf(',') + 1));
        // };
      } else {
        console.log("Error fetching PDF");
      }
    } catch (error) {
      console.error("Error fetching PDF", error);
    }
  };

  const modifyPaper = () => setisModifyPaper(true);

  const onClickClose = () => setisModifyPaper(false);

  const handleDownloadPdf = async () => {
    await fetchPdf();
    // const url = URL.createObjectURL(pdfBlob);
    const a = document.createElement("a");
    a.href = pdfString;
    a.download = "myDoc.pdf"; // Set your desired file name here
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(pdfString);
    a.remove();
    // setPdfString("");
  };

  const fetchSession = async () => {
    try {
      setApiResponse((prevApiResponse) => ({
        ...prevApiResponse,
        apiResponseStatus: apiStatusConstants.progess,
      }));
      // await fetchPdf();
      const jwtToken = Cookies.get("jwt_token");
      const url = `http://localhost:8082/api/abstract/${paperId}`;
      const options = {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwtToken}`,
        },
      };
      const response = await fetch(url, options);
      console.log(response);
      if (response.ok) {
        const data = await response.json();
        console.log(data);
        setApiResponse({
          apiResponseStatus: apiStatusConstants.success,
          data: data,
        });
      } else {
        setApiResponse((prevApiResponse) => ({
          ...prevApiResponse,
          apiResponseStatus: apiStatusConstants.failure,
        }));
        console.log("Error fetching session");
      }
    } catch (error) {
      setApiResponse((prevApiResponse) => ({
        ...prevApiResponse,
        apiResponseStatus: apiStatusConstants.failure,
      }));
      console.error("Error fetching session", error.message);
    }
  };

  useEffect(() => {
    fetchSession();
  }, []);

  const renderPaperLoadingView = () => (
    <div className="loader-container">
      <Loader type="ThreeDots" color="#00BFFF" height={80} width={80} />
    </div>
  );

  const renderModifyPaper = () => {
    return isModifyPaper ? <ModifyPaper id={paperId} /> : null;
  };

  const renderPaperSuccessView = () => {
    const { title, description, date, tags, documentVersions } =
      apiResponse.data;

    const version = documentVersions.length;
    //const version = documentVersions[documentVersions.length - 1].version;
    const reviewsList =
      version > 0 ? documentVersions[version - 1].reviews : [];

    return (
      <div className="component-container">
        <h1 className="component-title">{title}</h1>
        <h2>Description</h2>
        <p className="component-description">{description}</p>
        <p className="component-deadline">
          {" "}
          <span className="font-weight-bold">Deadline</span>: {deadline}
        </p>
        <p className="component-version">
          <span className="font-weight-bold">Version : </span>
          {version}
        </p>

        <p className="component-reviewers">
          <span className="font-weight-bold">
            Number of Reviewers(Reviewed)
          </span>
          : {numReviewers} out of 3
        </p>
        <span className="component-tags-heading font-weight-bold">Tags</span>
        <ul className="tags-list">
          {tags.map((tag) => (
            <li key={tag.id} className="component-tag">
              {tag.title}
            </li>
          ))}
        </ul>
        <div className="component-comments">
          <h2>Reviews:</h2>
          {reviewsList.length > 0 ? (
            <ul className="component-comment-item">
              {reviewsList.map((review) => (
                <li key={review.id}>{review.description}</li>
              ))}
            </ul>
          ) : (
            <p>No reviews yet</p>
          )}
        </div>
        <div className="paper-view-btn-container">
          <button
            type="button"
            className="component-button btn btn-primary cursor-pointer font-weight-bold mr-3"
            onClick={modifyPaper}
          >
            Modify Paper{" "}
          </button>
          <button
            type="button"
            className="component-button btn btn-primary cursor-pointer font-weight-bold"
            onClick={handleDownloadPdf}
          >
            Download Pdf{" "}
          </button>
          <Button
            onClick={onClickClose}
            variant="primary"
            type="submit"
            className="fw-5 ml-3 submit-button"
          >
            Close
          </Button>
        </div>
        {renderModifyPaper()}
      </div>
    );
  };

  const renderPaperFailureView = () => <ViewApiFailureView />;

  const renderPaperDetailedView = () => {
    const { apiResponseStatus } = apiResponse;
    switch (apiResponseStatus) {
      case apiStatusConstants.progess:
        return renderPaperLoadingView();
      case apiStatusConstants.success:
        return renderPaperSuccessView();
      case apiStatusConstants.failure:
        return renderPaperFailureView();
      default:
        return null;
    }
  };

  return (
    <div className="paper-detailed-view-main-container">
      {renderPaperDetailedView()}
    </div>
  );
};

export default PaperDetailedView;
