<template>
  <MainLayout>
    <div class="all-padding"></div>
    <div class="container" style="padding:0;">
      <div class="ai-container-in">
        <div class="w3-container w3-light-grey">
          <h2 class="text-center wow fadeInUp" style="padding-top: 30px; padding-bottom: 30px; font-weight: bold;" data-wow-duration="3s" data-wow-delay="0.5s">
            AI 퍼스널컬러 진단
          </h2>
        </div>
        <video autoplay muted loop class="ai-fullscreen-video" data-keepplaying>
          <source src="/img/AI_image/ai_video.mp4" type="video/mp4">
        </video>
        <div class="ai-flex-container">
          <div class="image-upload" style="margin: 0 auto;">
            <div class="image-frame">
              <div id="defaultImageContainer" v-show="!imageUploaded" @click="triggerFileInput">
                <img src="/img/Test_image/testframe.png" alt="기본 이미지" class="existing-image" style="height: 100%;" />
              </div>
              <div id="uploadedImageContainer" v-show="imageUploaded">
                <img id="uploadedImage" class="ai-image2" :src="uploadedImageUrl" alt="업로드된 이미지">
              </div>
              <button class="image-upload-btn" @click="openFileInput">
                사진 업로드
              </button>
            </div>
            <input type="file" ref="fileInput" class="ai-form-control mt-1" id="faceImage" name="faceImage" accept="image/*" @change="uploadImage" style="display:none">
          </div>
          <div class="ai-card1">
            <!-- <p class="ai-guideline">※ 사진 올리기 가이드라인 <br>
              ◎ 가이드라인을 잘 따르는 사진이 훨씬 나은 결과를 만들어냅니다. <br>
            </p> -->
            <img class="ai-image-guide" src="/img/AI_image/aiimageguide1.png">
            <div class="ai-guideline1">
              <!-- <p> ○ 증명사진, 얼굴이 선명하게 나온 사진, 조명에서 찍은 사진<br>
                Ｘ 전신 사진, 뿌연 사진, 과한 필터, 얼굴 가린 사진, 흔들린 사진, 안경낀 사진, 어두운 사진, 여러명이 나온 사진 </p> -->
            </div>
          </div>
        </div>
        <div class="ai-btn-grp" style="margin-top:100px;">
          <button class="ai-btn" @click="goToAiDiagResult">AI 진단 결과보기</button>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script>
import axios from "axios";
import MainLayout from '@/layouts/MainLayout.vue';

export default {
  name: 'AiDiagView',
  components: {
    MainLayout
  },
  data() {
    return {
      mnum:1,
      imageUploaded: false,
      uploadedImageUrl: "",
      maskmessage: "",
    }
  },
  methods: {
    uploadImage(event) {
      const file = event.target.files[0];
      if (file) {
        this.uploadedImageUrl = URL.createObjectURL(file);
        this.imageUploaded = true;

        const reader = new FileReader();
        reader.onload = (e) => {
          localStorage.setItem('befimg', e.target.result);
          localStorage.setItem('befimgn', file.name);
        };
        reader.readAsDataURL(file);

        this.formData = new FormData();
        this.formData.append('imgfile', file);

        axios.post(`${process.env.VUE_APP_DJANGO_APP_BACK_END_URL}/detect_mask`, this.formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        }).then((response) => {
          console.log('전송시작!');
          if (response.data.image) {
            const imageUrl = `data:image/jpeg;base64,${response.data.image}`;
            this.uploadedImageUrl = imageUrl;
            this.imageUploaded = true;
            this.maskmessage = response.data.message;
          } else {
            alert(response.data.message);
            this.imageUploaded = false;
          }
          console.log('전송완료!');
        }).catch((error) => {
          console.error('장고 서버로 이미지 전송 실패:', error);
          alert('이미지 전송 중 오류가 발생했습니다.');
          this.imageUploaded = false;
        });
      } else {
        alert('이미지를 선택해 주세요.');
      }
    },
    goToAiDiagResult() {
      if (this.maskmessage === "얼굴 영역에서 마스크 착용이 감지되었습니다.") {
        alert('마스크를 벗고 다시 사진을 찍어주세요.');
      } else if(this.maskmessage === "얼굴을 찾을 수 없습니다.") {
        alert('인물사진만 진단이 가능합니다.');
      } else if(this.maskmessage ==="얼굴 영역에서 마스크 미착용이 감지되었습니다.") {
        axios.get(`${process.env.VUE_APP_DJANGO_APP_BACK_END_URL}/seasontone`)
        .then((response) => {
          console.log(response.data.season)
          localStorage.setItem('season', response.data.season);
          
          this.$router.push('/AIResult');
        })
      } else {
        alert('사진을 업로드 해주세요.');
      }
    },
    triggerFileInput() {
      this.$refs.fileInput.click(); // 파일 업로드 input 클릭
    },
    openFileInput() {
      this.$refs.fileInput.click(); // 파일 업로드 input 클릭
    }
  }
}
</script>

<style scoped>
.ai-fullscreen-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border: 3px solid rgba(80, 79, 79, 0.116); /* 테두리 색과 두께 설정 */
  border-radius: 10px;  /* 테두리 모서리 둥글게 */
}

.ai-flex-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start; /* 수직 정렬 설정 (flex-start, center, flex-end 등) */
  gap: 20px; /* 두 칸 사이의 간격 */
  margin-top: 20px;
}

.image-upload, .ai-card1 {
  flex: 1; /* 각각 50% 너비 */
}

.image-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.image-frame {
  width: 100%;
  text-align: center;
}

.image-upload-btn {
  margin-top: 10px;
}

.ai-image2 {
  max-width: 100%;
}

.ai-card1 {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.ai-image-guide {
  max-width: 100%;
}
</style>
