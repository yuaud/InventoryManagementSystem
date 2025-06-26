import React, { useState, useEffect } from "react";
import Layout from "../component/Layout";
import ApiService from "../service/ApiService";
import { useNavigate, useParams } from "react-router-dom";

const AddEditProductPage = () => {
  const { productId } = useParams("");
  const [name, setName] = useState("");
  const [sku, setSku] = useState("");
  const [price, setPrice] = useState("");
  const [stockQuantity, setStockQuantity] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [description, setDescription] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [imageUrl, setImageUrl] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [categories, setCategories] = useState([]);
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const categoryData = await ApiService.getAllCategories();
        setCategories(categoryData.categories);
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Error at Fetching Categories: " + error
        );
        console.log(error);
      }
    };
    const fetchProductById = async () => {
      if (productId) {
        setIsEditing(true); 
        try {
          const productData = await ApiService.getProductById(productId);
          if (productData.status === 200) {
            console.log("buraya kadar okeyiz");
            setName(productData.product.name);
            setSku(productData.product.sku);
            setPrice(productData.product.price);
            setStockQuantity(productData.product.stockQuantity);
            setCategoryId(productData.product.categoryId);
            setDescription(productData.product.description);
            setImageUrl(productData.product.imageUrl);
          } else {
            showMessage(productData.message);
          }
        } catch (error) {
          showMessage(
            error.response?.data?.message ||
              "Error at Fetching Product By Id: " + error
          );
          console.log(error);
        }
      }
    };
    fetchCategories();
    if (productId) {
        console.log("ife girdik...");
        fetchProductById();
    };
  }, [productId]);

  //method to show message or errors
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setImageFile(file);
    const reader = new FileReader();
    reader.onloadend = () => setImageUrl(reader.result); //user imageUrl to preview the image to upload
    reader.readAsDataURL(file);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("name", name);
    formData.append("sku", sku);
    formData.append("price", price);
    formData.append("stockQuantity", stockQuantity);
    formData.append("categoryId", categoryId);
    formData.append("description", description);
    if (imageFile) formData.append("imageFile", imageFile);
    try {
      if (isEditing) {
        formData.append("productId", productId);
        await ApiService.updateProduct(formData);
        showMessage("Product Successfully Updated. Redirecting to Product Page");
      } else {
        await ApiService.addProduct(formData);
        showMessage("Product Successfully Saved. Redirecting to Product Page");
      }
      setTimeout(() => {navigate("/product")}, 1500);
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Error at Saving Product: " + error
      );
      console.log(error);
    }
  };

  return (
    <Layout>
        {message && <div className="message">{message}</div>}
        <div className="product-form-page">
            <h1>{isEditing ? "Edit Product" : "Add Product"}</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Product Name</label>
                    <input
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    type="text"
                    required
                    />
                </div>
                <div className="form-group">
                    <label>SKU</label>
                    <input
                    value={sku}
                    onChange={(e) => setSku(e.target.value)}
                    type="text"
                    required
                    />
                </div>
                <div className="form-group">
                    <label>Price</label>
                    <input
                    value={price}
                    onChange={(e) => setPrice(e.target.value)}
                    type="number"
                    required
                    />
                </div>
                <div className="form-group">
                    <label>Stock Quantity</label>
                    <input
                    value={stockQuantity}
                    onChange={(e) => setStockQuantity(e.target.value)}
                    type="number"
                    required
                    />
                </div>
                <div className="form-group">
                    <label>Description</label>
                    <textarea 
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                    >

                    </textarea>
                </div>
                <div className="form-group">
                    <label>Category</label>
                    <select
                    value={categoryId}
                    onChange={(e) => setCategoryId(e.target.value)}
                    required
                    >
                        <option>Select a category</option>
                        {categories.map((category) => (
                            <option key={category.id} value={category.id}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <label>Product Image</label>
                    <input type="file" onChange={handleImageChange}/>
                    {imageUrl && (
                        <img src={imageUrl} alt="preview" className="image-preview" />
                    )}
                </div>
                <button type="submit">
                    {isEditing ? "Edit Product" : "Add Product"}
                </button>
            </form>
        </div>
    </Layout>
  )

};

export default AddEditProductPage;
