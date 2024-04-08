var express = require('express');
var router = express.Router();

// them model
const Distributors = require('../models/distributors')
const Fruits = require('../models/fruits')

// api them distributor
router.post('/add-distributor', async (req, res) => {
    try {
        const data = req.body;
        const newDistributor = new Distributors({
            name: data.name
        });

        const result = await newDistributor.save();
        if (result) {
            res.json({
                "status": 200,
                "messenger": "Them thanh cong",
                "data": result
            })
        } else {
            res.json({
                "status": 400,
                "messenger": "Loi, them khong thanh cong",
                "data": []
            })
        }
    } catch (error) {
        console.log("Error: " + error);
    }
});

// api them fruit
router.post('/add-fruit', async (req, res) => {
    try {
        const data = req.body;
        const newfruit = new Fruits({
            name: data.name,
            quantity: data.quantity,
            price: data.price,
            status: data.status,
            image: data.image,
            description: data.description,
            id_distributor: data.id_distributor
        });
        const result = await newfruit.save();
        if (result) {
            res.json({
                "status": 200,
                "messenger": "Them thanh cong",
                "data": result
            })
        } else {
            res.json({
                "status": 400,
                "messenger": "Loi, them khong thanh cong",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
});
// get list distributor
router.get("/get-list-distributor", async (req, res) => {
    try {
        const data = await Distributors.find();
        if (data) {
            res.json({
                "status": 200,
                "messenger": "get thanh cong",
                "data": data
            })
        } else {
            res.json({
                "status": 400,
                "messenger": "not found",
                "data": []
            })
        }
    } catch (error) {

    }
})
// get name distributor
router.get("/get-distributor-name-by-id/:id", async(req, res) =>{
    try {
        const { id } = req.params;
        const distributor = await Distributors.findById(id);
        if (distributor) {
            res.json({
                "status": 200,
                "messenger": "Get distributor successfully",
                "data": distributor
            });
        } else {
            res.json({
                "status": 404,
                "messenger": "Distributor not found",
                "data": null
            });
        }
    } catch (error) {
        console.log(error);
        res.status(500).json({
            "status": 500,
            "messenger": "Internal server error",
            "data": null
        });
    }
})
// 
// get one fruit
router.get('/get-fruit-by-id/:id', async (req, res) => {
    try {
        const { id } = req.params
        const data = await Fruits.findById(id);
        res.json({
            "status": 200,
            "messenger": "Danh sach fruit",
            "data": data
        })
    } catch (error) {
        console.log(error);
    }
});
router.get('/get-list-fruit', async (req, res) => {
    const authHeader = req.headers['authorization']
    const token = authHeader && authHeader.split(" ")[1]
    if (token == null) return res.sendStatus(401);
    let payload;

    JWT.verify(token, SECRETKEY, (err, _payload) => {
        // kiem tra token, neu token ko dung , hoac het han
        // tra status code 403
        // tra status het han 401 khi token het han
        if (err instanceof JWT.TokenExpiredError) return res.sendStatus(401)
        if (err) return res.sendStatus(403)

        payload = _payload
    })
    console.log(payload)

    try {
        const data = await Fruits.find().populate("id_distributor");
        if (data) {
            res.json({
                "status": 200,
                "messenger": "Danh sach fruit",
                "data": data
            })
        } else {
            res.json({
                "status": 400,
                "messenger": "Not found",
                "data": []
            })
        }
        // console.log(data)

    } catch (error) {
        console.log(error);
    }
});
// get page fruit
router.get("/get-page-fruit", async (req, res) => {
    const authHeader = req.headers['authorization']
    const token = authHeader && authHeader.split(" ")[1]
    if (token == null) return res.sendStatus(401);
    let payload;

    JWT.verify(token, SECRETKEY, (err, _payload) => {
        if (err instanceof JWT.TokenExpiredError) return res.sendStatus(401)
        if (err) return res.sendStatus(403)
        payload = _payload
    })

    let perPage = 6; // so luong san pham hien tren 1 page
    let page = req.query.page || 1; //Page truyen len
    let skip = (perPage * page) - perPage; // phan trang
    let count = await Fruits.find().count(); // Lay tong so phan tu

    // filtering
    // loc theo name
    const name = { "$regex": req.query.name ?? "", "$options": "i" }
    // loc theo gia lon hon hoac bang gia truyen vao
    const price = { $gte: req.query.price ?? 0 }
    // loc sap xep theo gia
    // const sort = {price: req.query.sort ?? 1}
    // loc sap xep theo gia
    let sort = { price: 1 }; // Sắp xếp mặc định tăng dần
    // Kiểm tra giá trị của req.query.sort và gán lại nếu giá trị không hợp lệ
    if (req.query.sort === "1" || req.query.sort === "-1") {
        sort.price = parseInt(req.query.sort);
    }

    try {
        const data = await Fruits.find({ name: name, price: price })
            .populate("id_distributor")
            .sort(sort)
            .skip(skip)
            .limit(perPage);

        const convertData = data.map(item => ({
            ...item.toObject(),
            id_distributor: item.id_distributor._id.toString()
        }))

        if (convertData) {
            res.json({
                "status": 200,
                "messenger": "Danh sach fruit",
                "data": {
                    "data": convertData,
                    "currentPage": Number(page),
                    "totalPage": Math.ceil(count / page)
                }
            })
        } else {
            res.json({
                "status": 400,
                "messenger": "Not found",
                "data": []
            })
        }
        // console.log(data)
    } catch (error) {
        console.log(error)
    }
})


router.get('/get-list-fruit-in-price', async (req, res) => {
    try {
        const { price_start, price_end } = req.query

        const query = { price: { $gte: price_start, $lte: price_end } }

        const data = await Fruits.find(query, 'name quantity price id_distributor')
            .populate('id_distributor')
            .sort({ quantity: -1 })
            .skip(0)
            .limit(2)
        res.json({
            "status": 200,
            "messenger": "Danh sach fruit",
            "data": data
        })
    } catch (error) {
        console.log(error);
    }
});

router.get('/get-list-fruit-have-name-a-or-x', async (req, res) => {
    try {
        const query = {
            $or: [
                { name: { $regex: "A" } },
                { name: { $regex: "X" } },
            ]
        }
        const data = await Fruits.find(query, 'name quantity price id_distributor')
            .populate('id_distributor')

        res.json({
            "status": 200,
            "messenger": "Danh sach fruit",
            "data": data
        })
    } catch (error) {
        console.log(error);
    }
});

// xoa mot fruit
router.delete('/destroy-fruit-by-id/:id', async (req, res) => {
    try {
        const { id } = req.params
        const result = await Fruits.findByIdAndDelete(id);
        if (result) {
            res.json({
                "status": 200,
                "messenger": "Xoa thanh cong",
                "data": result
            })
        } else {
            res.json({
                "status": 400,
                "messenger": "Loi, xoa khong thanh cong",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
});
// add fruit with image
const Upload = require('../config/common/upload');
router.post('/add-fruit-with-file-image', Upload.array('image', 5), async (req, res) => {

    try {
        const data = req.body; // lay du lieu tu body
        const { files } = req  // files neu upload nhieu, file neu up load 1 anh
        const urlsImage = files.map((file) => `${req.protocol}://${req.get("host")}/uploads/${file.filename}`)
        // url anh se duoc luu duoi dang : http://localhost:3000/upload/filename
        const newFruit = new Fruits({
            name: data.name,
            quantity: data.quantity,
            price: data.price,
            status: data.status,
            image: urlsImage, // them url hinh
            description: data.description,
            id_distributor: data.id_distributor
        });
        const result = ((await newFruit.save()).populate("id_distributor")); // them vao database
        if (result) {
            res.json({
                "status": 200,
                "messenger": "Them thanh cong",
                "data": result
            })
        } else {
            res.json({
                "status": 400,
                "messenger": "Loi, them khong thanh cong",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
});
// update fruit 
router.put('/update-fruit-by-id/:id', Upload.array('image', 5), async (req, res) => {
    try {
        const { id } = req.params
        const data = req.body;
        const { files } = req;

        let urlImg;
        const updatefruit = await Fruits.findById(id)
        if (files && files.length > 0) {
            urlImg = files.map((file) => `${req.protocol}://${req.get("host")}/uploads/${file.filename}`);

        }
        if (urlImg == null) {
            urlImg = updatefruit.image;
        }

        let result = null;
        if (updatefruit) {
            updatefruit.name = data.name ?? updatefruit.name,
                updatefruit.quantity = data.quantity ?? updatefruit.quantity,
                updatefruit.price = data.price ?? updatefruit.price,
                updatefruit.status = data.status ?? updatefruit.status,

                updatefruit.image = urlImg,

                updatefruit.description = data.description ?? updatefruit.description,
                updatefruit.id_distributor = data.id_distributor ?? updatefruit.id_distributor,
                result = (await updatefruit.save()).populate("id_distributor");;
        }
        if (result) {
            res.json({
                'status': 200,
                'messenger': 'Cập nhật thành công',
                'data': result
            })
        } else {
            res.json({
                'status': 400,
                'messenger': 'Cập nhật không thành công',
                'data': []
            })
        }
    } catch (error) {
        console.log(error);
    }
})

const Users = require('../models/users');
const Transporter = require('../config/common/mail')
router.post('/register-send-email', Upload.single('avatar'), async (req, res) => {
    try {
        const data = req.body;
        const { file } = req
        const newUser = Users({
            username: data.username,
            password: data.password,
            email: data.email,
            name: data.name,
            avatar: `${req.protocol}://${req.get("host")}/uploads/${file.filename}`,
            role: data.role
        })
        const reuslt = await newUser.save();
        if (reuslt) {
            const mailOptions = {
                from: "longthph30891@fpt.edu.vn", // email gui di
                to: reuslt.email,  // email nhan
                subject: "Dang ky thanh cong", // subject
                text: "Thank you for register", // noi dung mail
            };
            await Transporter.sendMail(mailOptions);
            res.json({
                "status": 200,
                "messenger": "Them thanh cong",
                "data": reuslt
            })

        } else {
            res.json({
                "status": 400,
                "messenger": "Loi, them khong thanh cong",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
});

// api login
const JWT = require('jsonwebtoken');
const e = require('express');
const { token } = require('morgan');

const SECRETKEY = "FPTPOLYTECHNIC"

router.post('/login', async (req, res) => {
    try {
        const { username, password } = req.body;

        const user = await Users.findOne({ username, password });

        if (user) {
            // token nfuoi dung se du dung gui len header moi lan muon goi api
            const token = JWT.sign({ id: user._id }, SECRETKEY, { expiresIn: '1h' });
            // khi token het han , nguoi dung se call 1 api khac de lay token moi
            // truyen refeshToken len de lat ve 1 cap token , refeshToken moi
            // neu ca hai het han nguoi dung phai dang nhap lai
            const refeshToken = JWT.sign({ id: user._id }, SECRETKEY, { expiresIn: '1h' });
            // expiresIn thoi gian token
            res.json({
                "status": 200,
                "messenger": "Login successfully",
                "data": user,
                "token": token,
                "refeshToken": refeshToken,
                "role": user.role // tra ve vai tro nguoi dung (0,1)
                 
            })
        } else {
            res.json({
                "status": 400,
                "messenger": "Error login,account not found",
                "data": [],
            })
        }
        console.log(token)
    } catch (error) {
        console.log(error)
    }
})
// order
const Order = require("../models/Order")

router.post("/add-order", async(req, res) =>{
    try {
        const data = req.body;
        const newOrder = new Order({
            id_user: data.id_user,
            id_product: data.id_product,
            quantity: data.quantity,
            total: data.total,
            name_customer: data.name_customer,
            phone_customer: data.phone_customer,
            location: data.location,
            pay_method: data.pay_method,
            status: data.status
        })
        const result = await newOrder.save();
        if (result) {
            res.json({
                "status": 200,
                "messenger": "Them thanh cong",
                "data": result
            })
        } else {
            res.json({
                "status": 400,
                "messenger": "Loi, them khong thanh cong",
                "data": null
            })
        }
    } catch (error) {
        console.log(error);
    }
})

router.get("/get_list_order_user/:id_user", async (req, res) => {
    try {
        const id_user = req.params.id_user; 
        const result = await Order.find({ id_user: id_user, status: { $in: [0, 1, 2] } }); 

        if (result.length > 0) {
            res.json({
                "status": 200,
                "messenger": "Lấy danh sách thành công",
                "data": result
            });
        } else {
            res.json({
                "status": 400,
                "messenger": "Không tìm thấy đơn hàng phù hợp",
                "data": null
            });
        }
    } catch (error) {
        console.log(error);
        res.status(500).json({
            "status": 500,
            "messenger": "Lỗi server",
            "data": null
        });
    }
});
router.get("/get_list_order_history_user/:id_user", async (req, res) => {
    try {
        const id_user = req.params.id_user; 
        const result = await Order.find({id_user: id_user, status:{ $in: [3]} });
        if(result){
            res.json({
                "status": 200,
                "messenger": "Lay danh sach thanh cong",
                "data": result
            })
        }else{
            res.json({
                "status": 400,
                "messenger": "Loi lay danh sach",
                "data": null
            })
        }
    } catch (error) {
        console.log(error);
    }
});
router.get("/get_list_order_admin", async (req, res) => {
    try {
        const result = await Order.find({ status: { $in: [0, 1, 2] }});
        if(result){
            res.json({
                "status": 200,
                "messenger": "Lay danh sach thanh cong",
                "data": result
            })
        }else{
            console.log(error);
            res.status(500).json({
                "status": 500,
                "messenger": "Lỗi server",
                "data": null
            });
        }
    } catch (error) {
        console.log(error);
        res.status(500).json({
            "status": 500,
            "messenger": "Lỗi server",
            "data": null
        });
    }
});
router.get("/get_list_order_history_admin", async (req, res) => {
    try {
        const result = await Order.find({ status: { $in: [-1,3] }});
        if(result){
            res.json({
                "status": 200,
                "messenger": "Lay danh sach thanh cong",
                "data": result
            })
        }else{
            console.log(error);
            res.status(500).json({
                "status": 500,
                "messenger": "Lỗi server",
                "data": null
            });
        }
    } catch (error) {
        console.log(error);
        res.status(500).json({
            "status": 500,
            "messenger": "Lỗi server",
            "data": null
        });
    }
});
router.put("/update-order/:id", async(req, res) =>{
    try {
        const {id} = req.params
        const data = req.body;
        const newData = await Order.findById(id)
        let result = null;
        if(newData){
            newData.id_user = data.id_user ?? newData.id_user,
            newData.id_product = data.id_product ?? newData.id_product,
            newData.quantity = data.quantity ?? newData.quantity,
            newData.total = data.total ?? newData.total,
            newData.name_customer = data.name_customer ?? newData.name_customer,
            newData.phone_customer = data.phone_customer ?? newData.phone_customer,
            newData.location = data.location ?? newData.location,
            newData.pay_method = data.pay_method ?? newData.pay_method,
            newData.status = data.status ?? newData.status,
            result = await newData.save();
        }
        if (result) {
            res.json({
                "status": 200,
                "messenger": "Cap nhat thanh cong",
                "data": result
            })
        } else {
            res.json({
                "status": 400,
                "messenger": "Loi, cap nhat khong thanh cong",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
})

router.delete("/delete-order/:id", async(req, res) =>{
    try {
        const {id} = req.params
        const result = await Order.findOneAndDelete({id});
        if(result){
            res.json({
                "status": 200,
                "messenger": "Xoa thanh cong",
                "data": result
            })
        }else{
            res.json({
                "status": 400,
                "messenger": "Loi xoa",
                "data": null
            })
        }
    } catch (error) {
         console.log(error);
    }
})
module.exports = router;