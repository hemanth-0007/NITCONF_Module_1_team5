import React, { useState } from "react";
import "./index.css";

const RegisterForm = (props) => {
  const [formData, setFormData] = useState({
    name: "",
    username: "",
    email: "",
    password: "",
    gender: "",
    confirmPassword: "",
    errorMsg: "",
    showSubmitError: false,
  });

  const handleInputChange = (event) => {
    const target = event.target;
    const value = target.value;
    const stateName = target.id;
    setFormData(prevFormData => ({ ...prevFormData, [stateName]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    // TODO: handle form submission
    const {name , email , password,gender, confirmPassword, username} = formData;
    if (
      name === "" ||
      email === "" ||
      password === "" ||
      gender === "" ||
      confirmPassword === "" ||
      username === ""
    ) {
      setFormData({errorMsg: "Please enter all the details", showSubmitError: true});
      return;
    }
    const userDetails = {
      name,
      username,
      gender,
      email,
      password,
    };

    // Handle the validations here


    console.log(userDetails);
    alert("Registered Successfully");
    props.history.replace("/login");
  };

 

  return (
    <div className="register-form-container">
      <form className="form-container" onSubmit={handleSubmit}>
        <h1 className="register-main-heading">Registration</h1>
        <div className="input-container">
          <>
            <label className="input-label" htmlFor="name">
              FULL NAME
            </label>
            <input
              type="text"
              id="name"
              className="username-input-field"
              value={formData.name}
              onChange={handleInputChange}
              placeholder="FULL NAME"
            />
          </>
          <>
            <label className="input-label" htmlFor="username">
              USERNAME
            </label>
            <input
              type="text"
              id="username"
              className="username-input-field"
              value={formData.username}
              onChange={handleInputChange}
              placeholder="USERNAME"
            />
          </>
          {/* add the gender dropdown feild */}
          <>
            <label className="input-label">GENDER</label>
            <select
              value={formData.gender}
              onChange={handleInputChange}
              id="gender"
              className="username-input-field"
            >
              <option value="">Select Gender</option>
              <option value="male">Male</option>
              <option value="female">Female</option>
            </select>
          </>
          <>
            <label className="input-label" htmlFor="email">
              EMAIL
            </label>
            <input
              type="email"
              id="email"
              className="username-input-field"
              value={formData.email}
              onChange={handleInputChange}
              placeholder="EMAL ID"
            />
          </>
          <>
            <label className="input-label" htmlFor="password">
              PASSWORD
            </label>
            <input
              type="password"
              id="password"
              className="username-input-field"
              value={formData.password}
              onChange={handleInputChange}
              placeholder="PASSWORD"
            />
          </>
          <>
            <label className="input-label" htmlFor="confirmPassword">
              CONFIRM PASSWORD
            </label>
            <input
              type="password"
              id="confirmPassword"
              className="username-input-field"
              value={formData.confirmPassword}
              onChange={handleInputChange}
              placeholder="RE-ENTER YOUR PASSWORD"
            />
          </>
        </div>
        <button type="submit" className="login-button">
          REGISTER
        </button>
        {formData.showSubmitError && <p className="error-message">*{formData.errorMsg}</p>}
      </form>
    </div>
  );
};

export default RegisterForm;
