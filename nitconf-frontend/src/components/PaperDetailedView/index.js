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
import { v4 as uuidv4 } from "uuid";
import ReviewCard from "../ReviewCard";

const PaperDetailedView = (props) => {
  const apiStatusConstants = {
    initial: "INITIAL",
    progess: "PROGRESS",
    success: "SUCCESS",
    failure: "FAILURE",
  };

  const [pdfString, setPdfString] = useState("");
  const { paperId } = useParams();
  const deadline = "May 31, 2024";
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

  const modifyPaper = () =>
    setisModifyPaper((prevModifyPaper) => !prevModifyPaper);

  const onClickGoBack = () => props.history.goBack();

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
      <Loader type="Oval" color="#00BFFF" height={100} width={100} />
    </div>
  );

  const renderModifyPaper = () =>
    isModifyPaper ? <ModifyPaper id={paperId} /> : null;

  const renderPaperSuccessView = () => {
    const { title, description, tags, documentVersions } = apiResponse.data;
    const version = documentVersions.length;
    const reviewsList = [
      {
        id: uuidv4(),
        reviewer: "Reviewer 1",
        reviewContent: "This is good work will recommend for acceptance",
      },
      {
        id: uuidv4(),
        reviewer: "Reviewer 2",
        reviewContent:
          "There are some issues in the paper. Please correct them.",
      },
      {
        id: uuidv4(),
        reviewer: "Reviewer 3",
        reviewContent: "This is a good paper. Will recommend for acceptance.",
      },
    ];

    return (
      <div className="flex flex-row justify-center">
        <div className="h-auto p-5 w-2/4 bg-slate-100 rounded-3xl border-4 border-slate-600 m-5 overflow-auto">
          <h1 className="text-4xl fw-600 font-sans font-semibold mb-2">
            {title}
          </h1>
          <p className="text-xl font-normal font-sans mb-2">{description}</p>
          <p className="text-xl">
            {" "}
            <span className="font-weight-bold">Deadline</span>: {deadline}
          </p>
          <p className="component-version">
            <span className="font-weight-bold">Version : </span>
            {version}
          </p>
          <span className="component-tags-heading font-weight-bold">Tags</span>
          <ul className="list-none flex flex-row justify-start mb-3">
            {tags.map((tag) => (
              <li key={tag.id} className="mr-2 mt-1 text-lg p-1">
                {tag.title}
              </li>
            ))}
          </ul>
          <p className="text-xl font-semibold">Reviews</p>
          {reviewsList.length > 0 ? (
            <ul className="list-none">
              {reviewsList.map((review) => (
                <ReviewCard key={review.id} reviewDetails={review} />
              ))}
            </ul>
          ) : (
            <p className="">No reviews yet</p>
          )}

          <div className="mt-4">
            <Button
              className="bg-blue-600 font-semibold mr-2"
              type="button"
              onClick={modifyPaper}
              variant="primary"
            >
              {isModifyPaper ? "Close" : "Modify"}
            </Button>
            <Button
              className="bg-blue-600 font-semibold mr-2"
              type="button"
              onClick={handleDownloadPdf}
              variant="primary"
            >
              Download Pdf{" "}
            </Button>
            <Button
              className="bg-blue-600 font-semibold mr-2"
              type="button"
              onClick={onClickGoBack}
              variant="primary"
            >
              Go Back{" "}
            </Button>
          </div>
          {renderModifyPaper()}
        </div>
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
