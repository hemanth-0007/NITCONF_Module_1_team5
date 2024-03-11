import React, { useEffect, useState } from "react";
import "./index.css"; // Import CSS file for custom styling
import Header from "../Header";
import Cookies from "js-cookie";
import TagCard from "../TagCard";
import { v4 as uuidv4 } from "uuid";

const UploadAbstract = () => {
  // status constants
  const statusOptions = {
    pending: "PENDING",
    accepted: "ACCEPTED",
    rejected: "REJECTED",
  };

  // useState hooks
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    tags: [],
    selectedTag: "",
    file: null,
  });

  const [tagsList, setTagsList] = useState([]);
  const [titleError, setTitleError] = useState("");
  const [descriptionError, setDescriptionError] = useState("");
  const [formError, setFormError] = useState("");

  //sample tags list
  // const tagsList = [
  //   "Data Structures and Algorithms",
  //   "Operating Systems",
  //   "Database Management Systems",
  //   "Computer Networks",
  //   "Artificial Intelligence",
  //   "Machine Learning",
  //   "Web Development",
  //   "Mobile Application Development",
  //   "Geotechnical Engineering",
  //   "Transportation Engineering",
  // ];

  // useEffect hook to fetch tags list from backend
  useEffect(() => {
    const getTagsList = async () => {
      // const localStorageTagsList = JSON.parse(localStorage.getItem("tagsList"));
      // if (localStorageTagsList !== null) {
      //   setTagsList(localStorageTagsList);
      //   setFormData((prevFormData) => ({
      //     ...prevFormData,
      //     selectedTag: localStorageTagsList[0].title,
      //   }));
      //   return;
      // }
      const jwtToken = Cookies.get("jwt_token");
      const url = "http://localhost:8082/api/tags/findall";
      const options = {
        method: "GET",
        headers: {
          Authorization: `Bearer ${jwtToken}`,
        },
      };
      const response = await fetch(url, options);
      const data = await response.json();
      console.log(data);
      localStorage.setItem("tagsList", JSON.stringify(data));
      setTagsList(data);
      setFormData((prevFormData) => ({
        ...prevFormData,
        selectedTag: data[0].title,
      }));
    };
    getTagsList();

  }, []);

  // handleDescChange function to handle description change
  const handleDescChange = (event) => {
    const { value } = event.target;
    if (value.length > 300) setDescriptionError("Description cannot exceed 300 characters.");
    else {
      setDescriptionError("");
      setFormData((prevFormData) => ({ ...prevFormData, description: value }));
    }
  }
  // handleTitleChange function to handle title change
  const handleTitleChange = (event) => {
    const { value } = event.target;
    if (value.length > 50)  setTitleError("Title cannot exceed 50 characters.");
    else {
      setTitleError("");
      setFormData((prevFormData) => ({ ...prevFormData, title: value }));
    }
  };

  // handleFileChange function to handle file change
  const handleFileChange = (event) => {
    const file = event.target.files[0];
    console.log(file);
    if (file.type !== "application/pdf") {
      alert("Please upload a PDF file");
      return;
    }
    setFormData((prevFormData) => ({ ...prevFormData, file: file }));
  };

  // handleTagChange function to handle tag change
  const handleTagChange = (event) => {
    const { value } = event.target;
    setFormData((prevFormData) => ({ ...prevFormData, selectedTag: value }));
  };

  // onClickAddTag function to handle adding tags to the user's selected tags list
  const onClickAddTag = () => {
    const { selectedTag, tags } = formData;
    const newTag = {
      id: uuidv4(),
      title: formData.selectedTag,
    };
    let isTagInTagsList = tags.some((tag) => tag.title === selectedTag);
    if (selectedTag !== "" && !isTagInTagsList) {
      setFormData((prevFormData) => ({
        ...prevFormData,
        tags: [...prevFormData.tags, newTag],
      }));
    }
  };

  // handleSubmit function to handle form submission and send the data to the backend
  const handleSubmit = async (event) => {
    event.preventDefault();
    const { title, description, tags } = formData;
    if (
      title === "" ||
      tags.length === 0 ||
      description === ""
    ) {
      setFormError("Please enter all the feilds");
      return;
    }
    const updatedTags = tags.map((tag) => tag.title);
    const paperDetails = {
      title: title,
      description: description,
      status: statusOptions.pending,
      tags: updatedTags,
    };
    console.log(paperDetails);
    setFormData({
      title: "",
      description: "",
      tags: [],
      selectedTag: "",
    });
    const jwtToken = Cookies.get("jwt_token");
    const url = "http://localhost:8082/api/abstract";
    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify(paperDetails),
    };
    try {
      const response = await fetch(url, options);
      console.log(response);
      const data = await response.json();
      console.log(data);
      const sessionId = data.id;
      if (response.ok) {
        alert("Paper submitted successfully");
        await sendPdf(sessionId);
        return;
      } 
      else {
        alert("Error in submitting the paper");
        return;
      }
    } 
    catch (error) {
      console.log("err msg : ", error.message);
    }
  };

  const sendPdf = async (sessionId) => {
    const { file } = formData;
    const pdfData = new FormData();
    pdfData.append("file", file);
    const jwtToken = Cookies.get("jwt_token");
    const url = `http://localhost:8082/api/abstract/doc/${sessionId}`;
    const options = {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${jwtToken}`,
      },
      body: pdfData,
    };
    try {
      const response = await fetch(url, options);
      console.log("upload pdf response object : ",response);
      // const data = await response.json();
      // console.log(data);
      if (response.ok) {
        alert("PDF uploaded successfully");
        return;
      } else {
        alert("Error in uploading the PDF");
        return;
      }
    } catch (error) {
      console.log("err msg : ", error.message);
    }
  }
  // onDeleteTag function to handle deleting tags from the user's selected tags list
  const onDeleteTag = (id) => {
    const { tags } = formData;
    const updatedTags = tags.filter((tag) => tag.id !== id);
    setFormData((prevFormData) => ({ ...prevFormData, tags: updatedTags }));
  };

  const renderTitleField = () => {
    const { title } = formData;
    return (
      <>
        <label className="input-label" htmlFor="email">
          Title
        </label>
        <input
          type="text"
          id="email"
          className="username-input-field"
          value={title}
          onChange={handleTitleChange}
          placeholder="Title"
        />
      </>
    );
  };

  const renderDescField = () => {
    const { description } = formData;
    return (
      <>
        <label className="input-label" htmlFor="email">
          Description
        </label>
        <input
          type="text"
          id="email"
          className="username-input-field"
          value={description}
          onChange={handleDescChange}
          placeholder="Description"
        />
      </>
    );
  };

  const renderPdfField = () => {
    return (
      <>
        <label className="input-label" htmlFor="pdf-file">
          Upload PDF*:
        </label>
        <input
          type="file"
          id="pdf-file"
          accept=".pdf"
          className="username-input-field"
          onChange={handleFileChange}
          placeholder="Upload PDF"
        />
      </>
    );
  };

  

  return (
    <>
      <Header />
      <form className="my-form" onSubmit={handleSubmit}>
        <h1 className="mb-3 mt-3 fw-600 fs-2">Submit Paper </h1>

        <div className="input-container">
          {renderTitleField()}
          {titleError && <p className="text-danger">{titleError}</p>}
        </div>

        <div className="input-container">
          {renderDescField()}
          {descriptionError && (
            <p className="text-danger">{descriptionError}</p>
          )}
        </div>

        <div className="input-container">{renderPdfField()}</div>

        <div className="mb-3 d-flex flex-column justify-content-space-center align-class">
          <label className="form-label">Add Tags :</label>
          {tagsList.length > 0 ? (
            <select
              className="form-control"
              name="selectedTag"
              value={formData.selectedTag}
              onChange={handleTagChange}
            >
              {tagsList.map((tag) => (
                <option key={tag.id} value={tag.title}>
                  {tag.title}
                </option>
              ))}
            </select>
          ) : (
            // Can add some loader here
            <p>Loading the Tags</p>
          )}

          <button
            type="button"
            className="btn btn-outline-primary mt-3"
            onClick={onClickAddTag}
          >
            Add
          </button>
        </div>
        {/* displaying the selected tags */}
        <ul className="tags-list-group">
          {formData.tags.map((tag) => (
            <TagCard deleteTag={onDeleteTag} key={tag.id} tag={tag} />
          ))}
        </ul>

        <button type="submit" className="submit-btn btn btn-primary">
          {" "}
          Submit{" "}
        </button>
        <p>* feilds are compulsory </p>
        {formError && <p className="error-msg">{formError}</p>}
      </form>
    </>
  );
};

export default UploadAbstract;
