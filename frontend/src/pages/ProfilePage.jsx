import React, { useState, useEffect } from "react";
import Layout from "../component/Layout";
import ApiService from "../service/ApiService";

const ProfilePage = () => {
  const [user, setUser] = useState(null);
  const [message, setMessage] = useState(null);

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const userInfo = await ApiService.getLoggedInUserInfo();
        setUser(userInfo);
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Error at Getting User Info: " + error
        );
        console.log(error);
      }
    };
    fetchUserInfo();
  }, []);

  //method to show message or errors
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  return (
    <Layout>
      {message && <div className="message">{message}</div>}
      <div className="profile-page">
        {user && (
          <div className="profile-card">
            <h1>Hello, {user.name}</h1>
            <div className="profil-info">

              <div className="profile-item">
                <label>Name</label>
                <span>{user.name}</span>
              </div>
              <div className="profile-item">
                <label>Email</label>
                <span>{user.email}</span>
              </div>
              <div className="profile-item">
                <label>Phone Number</label>
                <span>{user.phoneNumber}</span>
              </div>
              <div className="profile-item">
                <label>Role</label>
                <span>{user.role}</span>
              </div>

            </div>
          </div>
        )}
      </div>
    </Layout>
  );
};

export default ProfilePage;
