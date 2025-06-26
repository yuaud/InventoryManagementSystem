import React, {useEffect, useState} from "react";
import Layout from "../component/Layout"
import ApiService from "../service/ApiService";

const CategoryPage = () => {
    const [categories, setCategories] = useState([]);
    const [categoryName, setCategoryName] = useState("");
    const [message, setMessage] = useState("");
    const [isEditing, setIsEditing] = useState(false);
    const [editingCategoryId, setEditingCategoryId] = useState(null);

    //fetch the categories from our backend
    useEffect(() => {
        const getCategories = async () => {
            try{
                const response = await ApiService.getAllCategories();
                if(response.status === 200){
                    setCategories(response.categories);
                }
            } catch(error) {
                showMessage(error.response?.data?.message || "Error at Getting Categories: "+ error);
                console.log(error);   
            }
        };
        getCategories();
    }, []);

    //Add Category
    const addCategory = async () => {
        if(!categoryName){
            showMessage("Category name cannot be empty.")
            return;
        }
        try{
            await ApiService.createCategory({name: categoryName});
            showMessage("Category successfully added");
            setCategoryName(""); // clear input
            setTimeout(() => {window.location.reload();}, 1500);
        } catch (error) {
            showMessage(error.response?.data?.message || "Error at Adding a Category: "+ error);
            console.log(error);   
        }
    };

    //Edit Category
    const editCategory = async () => {
        try {
            await ApiService.updateCategory(editingCategoryId, {name: categoryName});
            showMessage("Category Successfully Edited.")
            setIsEditing(false);
            setCategoryName(""); // clear input
            setTimeout(() => {window.location.reload();}, 1500);
        } catch (error) {
            showMessage(error.response?.data?.message || "Error at Editing a Category: "+ error);
            console.log(error);   
        }
    };

    //populate the edit category data (when you press edit button)
    const handleEditCategory = (category) => {
        setIsEditing(true);
        setEditingCategoryId(category.id);
        setCategoryName(category.name);
    };

    //delete category
    const handleDeleteCategory = async (categoryId) => {
        if(window.confirm("Are you sure you want to delete this category?")) {
            try {
                await ApiService.deleteCategory(categoryId);
                showMessage("Category Successfully Deleted.")
                window.location.reload(); // reload page
            } catch (error) {
                showMessage(error.response?.data?.message || "Error at Deleting a Category: "+ error);
                console.log(error);   
            }
        }
    };

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
            <div className="category-page">
                <div className="category-header">
                    <h1>Categories</h1>
                    <div className="add-cat">
                        <input type="text" value={categoryName} placeholder="Category Name" onChange={(e) => setCategoryName(e.target.value)}/>
                        {!isEditing ? (
                            <button onClick={addCategory}>Add Category</button>
                        ): (
                            <button onClick={editCategory}>Edit Category</button>
                        )}
                    </div>
                </div>
                {categories && (
                    <ul className="category-list">
                        {categories.map((category) => (
                            <li className="category-item" key={category.id}>
                                <span>{category.name}</span>
                                <div className="category-actions">
                                    <button onClick={() => handleEditCategory(category)}>Edit</button>
                                    <button onClick={() => handleDeleteCategory(category.id)}>Delete</button>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </Layout>
    )
}

export default CategoryPage;

