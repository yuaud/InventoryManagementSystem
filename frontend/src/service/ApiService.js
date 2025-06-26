import axios from "axios";
import CryptoJS from "crypto-js";

export default class ApiService {
  static BASE_URL = "http://localhost:8222/api";
  static ENCRYPTION_KEY = "yusuf-invertory-management";

  //encrypt data using crypto-js
  static encrypt(data) {
    return CryptoJS.AES.encrypt(data, this.ENCRYPTION_KEY.toString());
  }

  //decrypt data using crypto-js
  static decrypt(data) {
    const bytes = CryptoJS.AES.decrypt(data, this.ENCRYPTION_KEY);
    return bytes.toString(CryptoJS.enc.Utf8);
  }

  //save token with encryption
  static saveToken(token) {
    const encryptedToken = this.encrypt(token);
    localStorage.setItem("token", encryptedToken);
  }

  //retrieve the token
  static getToken() {
    const encryptedToken = localStorage.getItem("token");
    if (!encryptedToken) return null;
    return this.decrypt(encryptedToken);
  }

  //save role with encryption
  static saveRole(role) {
    const encryptedRole = this.encrypt(role);
    localStorage.setItem("role", encryptedRole);
  }

  //retrieve the role
  static getRole() {
    const encryptedRole = localStorage.getItem("role");
    if (!encryptedRole) return null;
    return this.decrypt(encryptedRole);
  }

  //when you log out clear the localStorage
  static clearAuth() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
  }

  //this is how you get token for sending Authorization with axios
  static getHeader() {
    const token = this.getToken();
    return {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    };
  }

  /** AUTH && USERS API  */

  // Register User
  static async registerUser(registerData) {
    const response = await axios.post(
      `${this.BASE_URL}/auth/register`,
      registerData
    );
    return response.data;
  }

  //Log in
  static async loginUser(loginData) {
    const response = await axios.post(`${this.BASE_URL}/auth/login`, loginData);
    return response.data;
  }

  //Get All Users
  static async getAllUsers() {
    const response = await axios.get(`${this.BASE_URL}/users/all`, {
      headers: this.getHeader(),
    });
    return response.data;
  }

  //Get Logged In User. (Also i'am using this for OpenFeign!)
  static async getLoggedInUserInfo() {
    const response = await axios.get(
      `${this.BASE_URL}/users/feign/getCurrentUser`,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Get User By Id
  static async getUserById(userId) {
    const response = await axios.get(`${this.BASE_URL}/users/${userId}`, {
      headers: this.getHeader(),
    });
    return response.data;
  }

  //Update the User
  static async updateUser(userId, userData) {
    const response = await axios.put(
      `${this.BASE_URL}/users/update/${userId}`,
      userData,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Delete the User
  static async deleteUser(userId) {
    const response = await axios.delete(
      `${this.BASE_URL}/users/delete/${userId}`,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  /** PRODUCT ENDPOINTS */

  //Add a Product
  static async addProduct(formData) {
    const response = await axios.post(
      `${this.BASE_URL}/products/add`,
      formData,
      {
        headers: {
          ...this.getHeader(),
          "Content-Type": "multipart/form-data",
        },
      }
    );
    return response.data;
  }

  //Update the Product
  static async updateProduct(formData) {
    const response = await axios.put(
      `${this.BASE_URL}/products/update`,
      formData,
      {
        headers: {
          ...this.getHeader(),
          "Content-Type": "multipart/form-data",
        },
      }
    );
    return response.data;
  }

  //Get All Products
  static async getAllProducts() {
    const response = await axios.get(`${this.BASE_URL}/products/all`, {
      headers: this.getHeader(),
    });
    return response.data;
  }

  //Get Product By Id
  static async getProductById(productId) {
    const response = await axios.get(`${this.BASE_URL}/products/${productId}`, {
      headers: this.getHeader(),
    });
    return response.data;
  }

  //Search Products
  static async searchProduct(searchValue) {
    const response = await axios.get(`${this.BASE_URL}/products/search`, {
      params: { searchValue },
      headers: this.getHeader(),
    });
    return response.data;
  }

  //Delete a Product
  static async deleteAProduct(productId) {
    const response = await axios.delete(
      `${this.BASE_URL}/products/delete/${productId}`,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  /* CATEGORY ENDPOINTS */

  //Create a Category
  static async createCategory(category) {
    const response = await axios.post(
      `${this.BASE_URL}/categories/add`,
      category,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Get All Categories
  static async getAllCategories() {
    const response = await axios.get(`${this.BASE_URL}/categories/all`, {
      headers: this.getHeader(),
    });
    return response.data;
  }

  //Get Category By Id
  static async getCategoryById(categoryId) {
    const response = await axios.get(
      `${this.BASE_URL}/categories/${categoryId}`,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Update a Category
  static async updateCategory(categoryId, categoryData) {
    const response = await axios.put(
      `${this.BASE_URL}/categories/update/${categoryId}`,
      categoryData,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Delete a Category
  static async deleteCategory(categoryId) {
    const response = await axios.delete(
      `${this.BASE_URL}/categories/delete/${categoryId}`,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  /** SUPPLIER ENDPOINTS */

  //Add a Supplier
  static async addSupplier(supplierData) {
    const response = await axios.post(
      `${this.BASE_URL}/suppliers/add`,
      supplierData,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Get All Suppliers
  static async getAllSuppliers() {
    const response = await axios.get(`${this.BASE_URL}/suppliers/all`, {
      headers: this.getHeader(),
    });
    return response.data;
  }

  //Get Supplier By Id
  static async getSuppliersById(supplierId) {
    const response = await axios.get(
      `${this.BASE_URL}/suppliers/${supplierId}`,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Update a Supplier
  static async updateSupplier(supplierId, supplierData) {
    const response = await axios.put(
      `${this.BASE_URL}/suppliers/update/${supplierId}`,
      supplierData,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Delete a Supplier
  static async deleteSupplier(supplierId) {
    const response = await axios.delete(
      `${this.BASE_URL}/suppliers/delete/${supplierId}`,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  /* TRANSACTION ENDPOINTS */

  //Purchase a Product
  static async purchaseProduct(body) {
    const response = await axios.post(
      `${this.BASE_URL}/transactions/purchase`,
      body,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Sell a Product
  static async sellProduct(body) {
    const response = await axios.post(
      `${this.BASE_URL}/transactions/sell`,
      body,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Return To Supplier
  static async returnToSupplier(body) {
    const response = await axios.post(
      `${this.BASE_URL}/transactions/return`,
      body,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Get All Transactions
  static async getAllTransactions(filter) {
    const response = await axios.get(`${this.BASE_URL}/transactions/all`, {
      headers: this.getHeader(),
      params: { filter },
    });
    return response.data;
  }

  //Get Transactions By Month And Year
  static async getTransactionsByMonthAndYear(month, year) {
    const response = await axios.get(
      `${this.BASE_URL}/transactions/by-month-and-year`,
      {
        headers: this.getHeader(),
        params: {
          month: month,
          year: year,
        },
      }
    );
    return response.data;
  }

  //Get Transaction By Id
  static async getTransactionById(transactionId) {
    const response = await axios.get(
      `${this.BASE_URL}/transactions/${transactionId}`,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  //Update a Transaction
  static async updateTransactionStatus(transactionId, status) {
    const response = await axios.put(
      `${this.BASE_URL}/transactions/${transactionId}`,
      status,
      {
        headers: this.getHeader(),
      }
    );
    return response.data;
  }

  /** GEMINI AI */
  static async getChatAnswer(message) {
    const response = await axios.post(`${this.BASE_URL}/gemini/chat`, message, {headers: this.getHeader()}
    );
    return response.data;
  }

  /** AUTHENTICATION CHECKER */
  static logout() {
    this.clearAuth();
  }

  static isAuthenticated() {
    const token = this.getToken();
    return !!token;
  }

  static isAdmin() {
    const role = this.getRole();
    return role === "ADMIN";
  }
}
