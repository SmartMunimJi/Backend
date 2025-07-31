// const mysql = require("mysql2/promise");

// exports.createPool = async () => {
//   try {
//     const pool = await mysql.createPool({
//       host: process.env.DB_HOST || "localhost",
//       user: process.env.DB_USER || "root",
//       password: process.env.DB_PASSWORD || "",
//       database: process.env.DB_NAME || "smart_munim_ji",
//       waitForConnections: true,
//       connectionLimit: 10,
//       queueLimit: 0,
//       ssl: {
//         rejectUnauthorized: false,
//       },
//     });

//     await pool.query("SELECT 1");
//     console.log("Database connection pool initialized successfully");
//     return pool;
//   } catch (err) {
//     console.error("Failed to initialize database connection pool:", {
//       message: err.message,
//       code: err.code,
//       errno: err.errno,
//       sqlState: err.sqlState,
//     });
//     throw err;
//   }
// };

// // module.exports = createPool;

const mysql = require("mysql2/promise");

const pool = mysql.createPool({
  host: process.env.DB_HOST || "localhost",
  user: process.env.DB_USER || "root",
  password: process.env.DB_PASSWORD || "",
  database: process.env.DB_NAME || "smart_munim_ji",
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0,
});

module.exports = pool;
