import React, { useState } from "react";
import "./index.css"; // Import CSS file for custom styling
import Header from "../Header";

const UploadAbstract = () => {
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    pdfFile: null,
    tags: [],
    selectedTag: "",
  });
  const tagsList = [
    "Data Structures and Algorithms",
    "Operating Systems",
    "Database Management Systems",
    "Computer Networks",
    "Artificial Intelligence",
    "Machine Learning",
    "Web Development",
    "Mobile Application Development",
    "Geotechnical Engineering",
    "Transportation Engineering",
  ];
  const [titleError, setTitleError] = useState("");
  const [descriptionError, setDescriptionError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    const maxLength = name === "title" ? 50 : 300; // Set max length based on field
    console.log(maxLength);
    if (value.length > maxLength) {
      if (name === "title") {
        setTitleError(`Title cannot exceed ${maxLength} characters.`);
      } else {
        setDescriptionError(
          `Description cannot exceed ${maxLength} characters.`
        );
      }
    } else {
      if (name === "title") {
        setTitleError("");
      } else {
        setDescriptionError("");
      }
      setFormData({
        ...formData,
        [name]: value,
      });
    }
  };

  const handleFileChange = (event) => {
    setFormData({
      ...formData,
      pdfFile: event.target.files[0],
    });
  };

  const handleTagChange = (event) => {
    setFormData({
      ...formData,
      selectedTag: event.target.value,
    });
  };

  const addTag = () => {
    if (
      formData.selectedTag !== "" &&
      !formData.tags.includes(formData.selectedTag)
    ) {
      setFormData((prevFormData) => ({
        ...prevFormData,
        tags: [...prevFormData.tags, prevFormData.selectedTag],
        selectedTag: "",
      }));
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    // Handle form submission logic here
    console.log(formData);
    // Reset form data after submission if needed
    setFormData({
      title: "",
      description: "",
      pdfFile: null,
      tags: [],
      selectedTag: "",
    });
  };

  return (
    <>
      <Header />
      <form className="my-form" onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Title* (max 50 characters):</label>
          <input
            type="text"
            className="form-control"
            name="title"
            value={formData.title}
            onChange={handleChange}
          />
          {titleError && <div className="text-danger">{titleError}</div>}
        </div>
        <div className="mb-3">
          <label className="form-label">
            Description* (max 300 characters):
          </label>
          <textarea
            className="form-control"
            name="description"
            value={formData.description}
            onChange={handleChange}
          />
          {descriptionError && (
            <div className="text-danger">{descriptionError}</div>
          )}
        </div>
        <div className="mb-3">
          <label className="form-label">Upload PDF*:</label>
          <input
            type="file"
            className="form-control p-1"
            accept=".pdf"
            onChange={handleFileChange}
            placeholder="Upload PDF"
          />
        </div>
        <div className="mb-3 d-flex flex-column justify-content-space-center align-class">
          <label className="form-label">Add Tags* :</label>
          <select
            value={formData.selectedTag}
            onChange={handleTagChange}
            className="form-select"
          >
            {tagsList.map((tag, index) => (
              <option key={index} value={tag}>
                {tag}
              </option>
            ))}
          </select>

          <button
            type="button"
            className="btn btn-outline-primary mt-3"
            onClick={addTag}
          >
            Add
          </button>
        </div>
        <ul className="tags-list-group">
          {
            // Display tags
            formData.tags.map((tag, index) => (
              <li key={index} className="tags-list-item">
                {tag}
              </li>
            ))
          }
        </ul>

        <button type="submit" className="btn btn-primary">
          Submit
        </button>
        <p>* feilds are compulsory </p>
      </form>
    </>
  );
};

export default UploadAbstract;
