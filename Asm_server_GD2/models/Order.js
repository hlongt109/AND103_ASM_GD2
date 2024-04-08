const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Orders = new Schema({
    id_user : {type: String},
    id_product: {type: String},
    quantity: {type: Number},
    total: {type:Number},
    name_customer: {type: String},
    phone_customer: {type: String},
    location: {type: String},
    pay_method: {type: String},
    status: {type:Number}
},{
    timestamps: true
})
module.exports = mongoose.model("order",Orders)