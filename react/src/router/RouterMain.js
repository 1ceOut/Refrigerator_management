import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Barcode from "../components/barcode";
import FoodList from "../components/foodlist";
import UserDataInput from "../components/UserDataInput";
import DetailFood from "../components/detailFood"; // 새로운 페이지 컴포넌트 임포트

const RouterMain = () => {
    return (
        <div>
            <br style={{ clear: 'both' }} />
            <Routes>
                <Route path="/" element={<Barcode />} />
                <Route path="/barlist" element={<FoodList />} />
                <Route path="/userinput" element={<UserDataInput />} />
                <Route path="/detailfood/:productName/:scategory/:productType" element={<DetailFood />} />
            </Routes>
        </div>
    );
};

export default RouterMain;
