const mongooes = require('mongoose');
mongooes.set('strictQuery' , true)

const url_local = "mongodb://127.0.0.1:27017/AND103"

const url_atalt = "mongodb+srv://hlong109:mESUqfMThzZx1zwj@cluster0.ryli2j3.mongodb.net/AND103"
const connect = async () =>{
    try {
        await mongooes.connect(url_local)
        console.log('Connect success');
    } catch (error) {
        console.log("error :"+ error);
        console.log('Connect failed');
    }
}
module.exports = {connect}