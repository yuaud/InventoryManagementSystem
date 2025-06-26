import React, { useState, useEffect } from "react";
import Layout from "../component/Layout";
import ApiService from "../service/ApiService";

const SalesPage = () => {
  const [products, setProducts] = useState([]);
  const [productId, setProductId] = useState("");
  const [description, setDescription] = useState("");
  const [note, setNote] = useState("");
  const [quantity, setQuantity] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const productData = await ApiService.getAllProducts();
        setProducts(productData.products);
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Error at Fetching Products: " + error
        );
        console.log(error);
      }
    };
    fetchProducts();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!productId || !quantity) {
      showMessage("Please fill in all required fields");
    }
    const body = {
      productId,
      quantity: parseInt(quantity),
    };
    try {
      const response = await ApiService.sellProduct(body);
      showMessage(response.message);
      resetForm();
    } catch (error) {
      showMessage(
        error.response?.data?.message ||
          "Error at Selling a Product: " + error
      );
      console.log(error);
    }
  };

  const resetForm = () => {
    setProductId("");
    setDescription("");
    setNote("");
    setQuantity("");
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
      <div className="purchase-form-page">
        <h1>Sell Inventory</h1>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Select Product</label>
            <select
              value={productId}
              onChange={(e) => setProductId(e.target.value)}
              required
            >
              <option>Select a product</option>
              {products.map((product) => (
                <option key={product.id} value={product.id}>
                  {product.name}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Quantity</label>
            <input
              value={quantity}
              onChange={(e) => setQuantity(e.target.value)}
              type="number"
              required
            />
          </div>
          <div className="form-group">
            <label>Description</label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            ></textarea>
          </div>
          <div className="form-group">
            <label>Note</label>
            <textarea
              value={note}
              onChange={(e) => setNote(e.target.value)}
            ></textarea>
          </div>

          <button type="submit">Sell Product</button>
        </form>
      </div>
    </Layout>
  );
};

export default SalesPage;
