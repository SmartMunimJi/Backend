const pool = require("../config/db.config");

class SellerModel {
  static async findActiveSellers() {
    const [rows] = await pool.query(
      `SELECT seller_id, shop_name
       FROM sellers
       WHERE contract_status = 'ACTIVE' AND api_base_url IS NOT NULL AND api_key IS NOT NULL`
    );
    return rows;
  }

  static async findById(sellerId) {
    const [rows] = await pool.query(
      `SELECT * FROM sellers WHERE seller_id = ?`,
      [sellerId]
    );
    return rows[0];
  }

  static async findSellersByCustomerProducts(customerUserId) {
    const [rows] = await pool.query(
      `SELECT DISTINCT s.seller_id, s.shop_name
       FROM sellers s
       JOIN customer_registered_products crp ON s.seller_id = crp.seller_id
       WHERE crp.customer_user_id = ?`,
      [customerUserId]
    );
    return rows;
  }
}

module.exports = SellerModel;
