import React, {useState} from "react";
import { useNavigate } from "react-router-dom";
import ApiService from "../service/ApiService";

const LoginPage = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    const handleLogin = async(e) => {
        e.preventDefault();
        try{
            const loginData = {email, password}
            const response = await ApiService.loginUser(loginData);
            console.log(response);
            
            if (response.status === 200) {
                ApiService.saveToken(response.token);
                ApiService.saveRole(response.role);
                setMessage(response.message);
                navigate("/dashboard")
            }
        } catch(error) {
            showMessage(error.response?.data?.message || "Error at Logging in a User: "+ error);
            console.log(error);            
        }
    };
    const showMessage = (msg) => {
        setMessage(msg);
        setTimeout(() => {
            setMessage("");
        }, 4000);
    };
    
    return(
        <div className="auth-container">
            <h2>Login</h2>

            {message && <p className="message">{message}</p>}

            <form onSubmit={handleLogin}>

                <input 
                type="email" 
                name="" 
                placeholder="Email" 
                value={email} 
                onChange={(e) => setEmail(e.target.value)}
                required
                />

                <input 
                type="password" 
                name="" 
                placeholder="Password" 
                value={password} 
                onChange={(e) => setPassword(e.target.value)}
                required
                />

                <button type="submit">Login</button>
            </form>
            <p>Don't have an account? <a href="/register">Register</a></p>
        </div>
    );
};

export default LoginPage;
