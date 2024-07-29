import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Barcode from "../components/barcode";
import FoodList from "../components/foodlist"; // 새로운 페이지 컴포넌트 임포트

const RouterMain = () => {
    return (
        <div>
            <br style={{ clear: 'both' }} />
            <Routes>
                <Route path="/" element={<Barcode />} />
                <Route path="/saved-barcodes" element={<FoodList />} />
            </Routes>
        </div>
    );
};

export default RouterMain;
