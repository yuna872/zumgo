import axios from "axios";
import React from "react";
import styles from "./SellerLoading.module.css";
import sellergo from "../../assets/images/sellergo.png";
import { ChevronLeftIcon } from "@heroicons/react/24/outline";
import { useNavigate } from "react-router-dom";

export default function SellerLoading({ joinSession, roomId, title }) {
  const token = window.localStorage.getItem("token");
  const navigate = useNavigate();

  const onairSession = () => {
    axios
      .patch(`https://i8c110.p.ssafy.io/api/v1/talk/start/${roomId}`)
      .then((res) => console.log(res, "π"))
      .catch((err) => console.log(err));

    //   .catch((err) => console.log(err));
    // const body = JSON.stringify({
    //   productId: roomId,
    //   liveStartTime: new Date(),
    //   liveStatus: "ONAIR",
    // });

    // axios
    //   .patch(`https://i8c110.p.ssafy.io/api/v1/live/${roomId}`, body, {
    //     headers: {
    //       Authorization: token,
    //       "Content-Type": "application/json",
    //     },
    //   })
    //   .then((res) => console.log(res))
    //   .catch((err) => console.log(err));
  };

  return (
    <div className={styles.back}>
      <div className={styles.navleft}>
        <ChevronLeftIcon
          className="w-6 h-6 text-white"
          onClick={() => {
            navigate("/live");
          }}
        />
      </div>
      <div className={styles.body}>
        <div className={styles.title}>"{title}"</div>
        <div
          className={styles.live}
          onClick={() => {
            onairSession();
            joinSession();
          }}
        >
          λΌμ΄λΈ μμνκΈ°
        </div>
      </div>

      <div className={styles.tutorial}>
        <div className={styles.p1}>
          <div className={styles.p11}>
            1. κ΅¬λ§€μλ€μκ² μν μ€λͺμ λ§μΉ νμ,
          </div>
          <div className={styles.p11}>
            <img src={sellergo} alt="go" className={styles.goicon} />
            <span className={styles.span}> λ²νΌμ λλ¬</span>
          </div>
          <div>30μ΄ λμ κ΅¬λ§€ μμ¬λ₯Ό νμΈνμΈμ!</div>
        </div>

        <div className={styles.p1}>
          <div className={styles.p11}>
            2. κ΅¬λ§€νκ³  μΆμ μ¬λμ΄ 2λͺ μ΄μμ΄λ©΄,
          </div>
          <div>μ€κ³ λ§μ λ―Έλ κ²½λ§€κ° μμλ©λλ€!</div>
        </div>
        <div>κ²½λ§€λ₯Ό ν΅ν΄ λ¬Όκ±΄μ νμλ³΄μΈμπ</div>
      </div>
    </div>
  );
}
