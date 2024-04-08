const mongoose = require('mongoose');
const Scheme = mongoose.Schema;

const Users = new Scheme({
    username: {type: String, unique: true, maxLength: 255},
    password: {type: String, maxLength: 255},
    email: {type: String, unique: true},
    name: {type: String},
    avatar: {type: String},
    available: {type: Boolean, default: false},
    role: {type: Number, default: 1}
},{
    timestamps: true
})
module.exports = mongoose.model("user",Users);