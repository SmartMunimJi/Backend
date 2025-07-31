const pool = require("../config/db.config");
const moment = require("moment");

class CustomerRegisteredProductModel {
  static async create({
    customerUserId,
    sellerId,
    sellerOrderId,
    sellerCustomerPhoneAtSale,
    productName,
    productPrice,
    dateOfPurchase,
    warrantyValidUntil,
  }) {
    const [result] = await pool.query(
      `INSERT INTO customer_registered_products (customer_user_id, seller_id, seller_order_id, seller_customer_phone_at_sale, product_name, product_price, date_of_purchase, warranty_valid_until)
       VALUES (?, ?, ?, ?, ?, ?, ?, ?)`,
      [
        customerUserId,
        sellerId,
        sellerOrderId,
        sellerCustomerPhoneAtSale,
        productName,
        productPrice,
        dateOfPurchase,
        warrantyValidUntil,
      ]
    );
    return result.insertId;
  }

  static async findByCustomerId(customerUserId) {
    const [rows] = await pool.query(
      `SELECT crp.registered_product_id, crp.product_name, s.shop_name, crp.seller_order_id, crp.date_of_purchase, crp.warranty_valid_until
       FROM customer_registered_products crp
       JOIN sellers s ON crp.seller_id = s.seller_id
       WHERE crp.customer_user_id = ?`,
      [customerUserId]
    );
    return rows.map((row) => ({
      registeredProductId: row.registered_product_id,
      productName: row.product_name,
      shopName: row.shop_name,
      sellerOrderId: row.seller_order_id,
      dateOfPurchase: row.date_of_purchase,
      warrantyValidUntil: row.warranty_valid_until,
      isWarrantyEligible: moment().isSameOrBefore(row.warranty_valid_until),
      daysRemaining: moment(row.warranty_valid_until).diff(moment(), "days"),
    }));
  }

  static async findById(registeredProductId) {
    const [rows] = await pool.query(
      `SELECT * FROM customer_registered_products WHERE registered_product_id = ?`,
      [registeredProductId]
    );
    return rows[0];
  }
}

module.exports = CustomerRegisteredProductModel;
