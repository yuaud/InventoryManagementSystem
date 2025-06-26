import React, { useState, useEffect } from "react";
import Layout from "../component/Layout";
import ApiService from "../service/ApiService";
import { useNavigate } from "react-router-dom";
import PaginationComponent from "../component/PaginationComponent";

const ProductPage = () => {
  const [products, setProducts] = useState([]);
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  //Pagination Set-up
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const itemsPerPage = 10;

  useEffect(() => {
    const getProducts = async () => {
      try {
        const productData = await ApiService.getAllProducts();
        if (productData.status === 200) {
          setTotalPages(Math.ceil(productData.products.length / itemsPerPage));
          setProducts(
            productData.products.slice(
              (currentPage - 1) * itemsPerPage,
              currentPage * itemsPerPage
            )
          );
        }
      } catch (error) {
        showMessage(
          error.response?.data?.message || "Error at Getting Products: " + error
        );
        console.log(error);
      }
    };
    getProducts();
  }, [currentPage]);

  //Delete a product
  const handleDeleteProduct = async (productId) => {
    if (window.confirm("Are you sure you want to delete this Product?")) {
      try {
        await ApiService.deleteAProduct(productId);
        showMessage("Product Successfully Deleted.");
        //window.location.reload(); // reload page
        setTimeout(() => {window.location.reload()}, 1500);
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Error at Deleting a Product: " + error
        );
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
        <div className="product-page">
            <div className="product-header">
                <h1>Products</h1>
                <button
                className="add-product-btn"
                onClick={() => navigate("/add-product")}
                >
                    Add Product
                </button>
            </div>
            {products && (
                <div className="product-list">
                    {products.map((product) => (
                        <div key={product.id} className="product-item">
                            <img 
                            src={product.imageUrl}
                            alt={product.name}
                            className="product-image"
                            />
                            <div className="product-info">
                                <h3 className="name">{product.name}</h3>
                                <p className="sku">Sku: {product.sku}</p>
                                <p className="price">Price: {product.price}</p>
                                <p className="quantity">Quantity: {product.stockQuantity}</p>
                            </div>
                            <div className="product-actions">
                                <button className="edit-btn" onClick={() => navigate(`/edit-product/${product.id}`)}>Edit</button>
                                <button className="delete-btn" onClick={() => handleDeleteProduct(product.id)}>Delete</button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
        <PaginationComponent
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
        />
    </Layout>
  )

};


export default ProductPage;
