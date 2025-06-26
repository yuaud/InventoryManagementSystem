import React from "react";
import Sidebar from "./Sidebar";
import GeminiAI from "./GeminiAI";

const Layout = ({children}) => {
    return(
        <div className="layout">
            <Sidebar/>
            <div className="main-content">
                {children}
            </div>
            <GeminiAI/>
        </div>
    );
}



export default Layout;
