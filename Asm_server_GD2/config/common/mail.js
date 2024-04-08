var nodemailer = require('nodemailer');
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth:{
        user : "longthph30891@fpt.edu.vn", // email gui di
        pass: "whpj dkvg vqhy zmko", // mat kwhpj dkvg vqhy zmkohau email gui di
    }
});
module.exports = transporter;