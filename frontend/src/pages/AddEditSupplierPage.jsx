import React, { useState, useEffect } from "react";
import Layout from "../component/Layout"
import ApiService from "../service/ApiService";
import { useNavigate, useParams } from "react-router-dom";

const AddEditSupplierPage = () => {
    const {supplierId} = useParams("");
    const [name, setName] = useState("");
    const [contactInfo, setContactInfo] = useState("");
    const [address, setAddress] = useState("");
    const [message, setMessage] = useState("");
    const [isEditing, setIsEditing] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        if(supplierId){
            setIsEditing(true);
            const fetchSupplier = async () => {
                try {
                    const response = await ApiService.getSuppliersById(supplierId);
                    if(response.status === 200) {
                        setName(response.supplier.name);
                        setContactInfo(response.supplier.contactInfo);
                        setAddress(response.supplier.address);
                    }
                } catch (error) {
                    showMessage(error.response?.data?.message || "Error at Getting a Supplier By Id: "+ error);
                    console.log(error); 
                }
            }
            fetchSupplier();
        }
    }, [supplierId]);

    //handle form submission for both add and edit supplier
    const handleSubmit = async (e) => {
        e.preventDefault();
        const supplierData = {name, contactInfo, address}
        try {
            if(isEditing){
                await ApiService.updateSupplier(supplierId, supplierData);
                showMessage("Supplier Edited Successfully");
                setTimeout(() => {navigate("/supplier");}, 1500);
            } else {
                await ApiService.addSupplier(supplierData);
                showMessage("Supplier Added Successfully");
                setTimeout(() => {navigate("/supplier");}, 1500);
            }
        } catch (error) {
            showMessage(error.response?.data?.message || "Error at Editing/Adding a Supplier: "+ error);
            console.log(error); 
        }
    }

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
            <div className="supplier-form-page">
                <h1>{isEditing ? "Edit Supplier" : "Add Supplier"}</h1>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Supplier Name</label>
                        <input 
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                        />
                    </div>
                    <div className="form-group">
                        <label>Contact Info</label>
                        <input 
                        type="text"
                        value={contactInfo}
                        onChange={(e) => setContactInfo(e.target.value)}
                        required
                        />
                    </div>
                    <div className="form-group">
                        <label>Address</label>
                        <input 
                        type="text"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                        required
                        />
                    </div>
                    <button type="submit">{isEditing ? "Edit Supplier" : "Add Supplier"}</button>
                </form>
            </div>
        </Layout>
    );
}

export default AddEditSupplierPage;