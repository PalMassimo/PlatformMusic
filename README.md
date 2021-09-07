# PlatformMusic

## Table of Contents
- [Introduction](#Introduction)
- [Goal](#goal)
- [How to use](#how-to-use)
- [The Application](#the-application)
- [What next?](#what-next)
- [Author](#author)
- [Tools](#tools)

### Introduction
This repository contains the code of the final android project for the Integrated and Mobile System [course](https://corsi.units.it/in20/insegnamento/sistemi-integrati-e-mobili-226mi-2017) at [University of Trieste](https://www.units.it/en).

<p align= center>
<img src= "https://www.emploidakar.com/wp-content/uploads/2020/07/Android-free-online-courses.png" height=350 width=700
>
</p>

---

### Goal
The goal of the project is to provide a platform where users can share songs to each other.

<p align= center>
<img src= "https://thumbs.dreamstime.com/z/man-listening-to-music-earphone-headphone-cliparts-icons-set-human-pictogram-representing-people-song-various-45286175.jpg" height=350 width=350
>
</p>

---

### How to use
To use the application go [here](https://github.com/PalMassimo/PlatformMusic/tree/main/build/outputs/apk/debug) and download the `music_platform.apk`. Then, you just have to install it on your android device. 

---

### The Application
Each user can upload a music post, in which he has to provide the song, the name of the song itself and the artist who wrote it. Additionally, he can add a song picture.

The music posts can be played from the home page of the application and the user can add a preference to it. Moreover, the song can be download to his/her android device.


---

### Author
The whole code is developed and manteined by Massimo Palmisano

---

### Tools
The whole code is written in Kotlin. The data are stored in Realtime Database, provided by Firebase. Instead, to store files Firebase Storage is used. Firebase is again used to user authentication. 

<p>
<img src= "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAT4AAACfCAMAAABX0UX9AAAA/FBMVEX////8yj9jZmr2ggxgY2f9phL/pQ76pBJcX2Pj4+Tr6+xeYWZydHivsLJXWl+bnZ9qbXH09PSUlZh/gYW7vL3V1tf8zkRsb3Pu7u/4nwCJi454e37IycqipKbb3N38yDb8yS22t7n1vDz5sDb5qTL//vn98d/+367/7Mn/++v+67NRVFn2gw/umQnMzc6eoKL4mhn91pf4qhP94Lr5sjv+3KT8vlL/9uL+z4P/57r9x27/8db9x3P8qif9wGH9zX3+78D92Gz94Iv91l7yjAf/9Mz96KL4zW790Y79v1X945L8z1H93Xv1sUn3oCb+8cT40oL2w1X1uyX41o6GvQYeAAALZklEQVR4nO2ce3/TOBaGE1etlThxfElIfIlNgN6gTVsoA0y57Q47nV2W2aHz/b/L6kjyXW5jh9+kmZ7nr9aKFfnta52LDZ0OgiAIgiAIgiAIgiAIgiAIgiAIgiAIgiAIgiAIgiAIgiAIgiAIct/Z3/QCtpoL7+XRptewvbx55nlPN72I7eVI13XvcNOr2FpeeUy+y+NNL2NbufDAfq83vYxt5SeQT3+G9mvHS1BP9y42vY4tZY/Lp+9i9teGN56Qz3u76ZVsJe+kfPqHN5teyjZykcjn/bzppWwjbxP59N2r9ODVFe6EK7H/kcm3u7sL9nufHDz09nAnXInjpzqXj+snOwcHH/b2vKvbz0M4B7tCPdDP+yiOvd1jvL/9PIRz5WXy6bvv4NCxB/LtYRNrBQ5T+cB+l3CIm29v72P5o8cYTip8ysune+/gdhbyle33+es/MDMs81MmH7fffudnqd7eZeGDvz7uxv/c0CLvL5deqh633+GBnsjnvcp97nnc7XZ/2dgy7ynHH/SCfPrT914i3963bLN7/pip1403uNJ7yZFeku/8y2lqv720hc+91+0+xqZgkVzgFfJ9OcnJ90F+6jP3HpPvXxtd7P3jdUm+852dvP1EC/+5VK8b/6qexR4rWPTYyNKfOv2mi5rB6ZN1Luuv4lMh8Or6yc5O3n68hf+8m1AXevuWSSu8CJl6xCB0Omu4qJ7Gzp6ve2l/AfuXRfmY+cB+afSAFn7qPSbff9XT9AdEq0CZfC4cp8OGq+pZLU7aBPsv9YJ8JyDfTmY/79mBjBqCX9SxQymfyeSz4LjhNFzV1sh3rDBfbvfz9NPrbp5H6rpDyEeKgHy+ATr+bd135CnMt3NyfirU806fPCrIF6vbWCCfMVIMRBolNLAbrmpr5LsoVLzSfIn99Ip63fi5cppa+Tq9kT9vqt72yPcp365KzMc41z2VenWht16+VmyNfB/z8n1J1QP7qdSrq3ofqHzH3/LyZeZj9js9VanXfaSc54HKd/VBbT6wn1K9bqzMXB6ofEfJgw69ZD6mn1K9bqyseuvls4HiT3YvHIdREk9mk+HId+bjXi7AZPLNlkMHBhVfGQ5Hzny4LFc0dj+cj5yzcVQOWHZvfOY4o0VloD2vvPQpW9F8OzuXSvVqQm+tfLbj+67w0cj3fSjExi4r8MwX4qA9HJjUIAar8dwwPSuRrz9nxaABg/6yOO/EMcSIaY0K2oY+HzCoGYwLZ4QuZV/EvskcDH+UgL9l8p0XzfdUrV5N06BevimreUXR4bIM0GefNHmBYvKLDllamBQpxBwkQnD5Fp2xSbNBP3fN9sg00uLGyGXlfdfM5qNaJmyvMECKyrbm3x6v2arme1ajHstcVM+L6uWDASkf0YgzC8R1kwEcGnPxmCMMfpRYkbxYkC8cUznIL5z6aePGdris6VBaFPYHhjhOxcTJfJ3elIgzmP+4gIsfIt9LLp9eMV+teixzWUe+EbtwQk3ThG5MJ6TCdM585BO4YGL1Uvk0HzQz3dHcEYbNihefctMFo/ko4EOm7M64vEQMzhaLecDnG9jp8tgEmj8/c6Zish/iv93k9Zai+eruXOCRKvRy+Ry7SFU+bcouOxhGvSi0oS3FjlBX7Gr2GAxCgkw+uMqRCAwTLhiVf5/QhF8c4awer6opbw4uYcCSW2gUkFRXkJVYcssTkxFFNGrKm/TtIH1F73W7j1VNA/7nnfpunrlCPrjsNFQ6BqiXbml9uK/MMJWP/ZLFixFNRYItkdBRYcjw+U+G6JLJ+SzmXX4GuJzQKD0D9GvcBVJw6KnMd5v3akKvouNC+RWV5aPZqifMK8TNBYQIpuB3qJCPhLlvmNPUmxBF3dyQDxP3kx+yCZdz8Zey+caXqdexYSnm+vZLX+07X9V7TL7/KCZS9PuISj4SZGkaN1+hIw8ScZNw+WhhL7XhXiTJx+18treEETAqyGdUnwzAzU4LvetJ5UgrfvOq5rtDPXXDmctHCq16UyVfLuLBKULi7JAps2UhX1EIiMOlS7Zn3GqzqQwFQ3CoX0nqHFhbMbtmfwvilj/XFP5qX8l8t9+5gCr0ci2CYQEe20rymZkkE2Y+Y96L8mhyFwP5StJ2bJKkO/y3ySgAj1vuMILvEKpDFDGseVTQCoaJW/ieiPmUTBs/wSrBX+0rmu8u7zG+KkIvj7xniq8oy5eNjEX2UUCT+5uy5g1y54cBlfk2oYaffnrI80FKB/5wkppQhPHiF/Fzo856HOyWzXe39xiKqnfFvE/TspE5Le+WMp+2pXzlzAzuQVPcrb64fh6i2N9AS8UeEjnAcks3lImK8ntyG2lbruS9e9JIvfhze/msknxG9fGmJeUzwtJUZ4a8+W1ZW0wD1w0GmnCc9GrkkqQMNIwBF2gi6owKZqmMbozMW84b3LldddXbUj4yH1ZY1MkHWZ0Ju5rIAYOQ7112NOTZd3qrT3ibQdZooZSP+NUvGq6bubz3CuZbyXvq0NtCvgVE0porkC2DIpCWvIBBXjTkvoxH3vxOOQvPfG5KAvtbpCk20h8Bbxik5lvNe4yv1ZlayLek+RKhCHdfeTqeSndkepJPt9PIm//e3jB5wtyfQq/iLi1acOnlzLeyet3H1ZlayBepJJLwxGVaTOAiQyYz4MLCvjVR2yvie6ItY9aP65EmvHmWM9+Kdy6Xr1r1tpCPV05aIfeyk2xDpM3F0MsL2rGcx5yVR7h8dphPnqCsMSNZzBS3gknTt24UHOV2vtW9x+T7XJmqhXz8JqSFm2rxwl1wAUWqNihUZmC+aVLZ5gvWCZXu6w8D08wlc/AF8HvP1EpJcp9O52uXvNCplxlzA+8pQ++t8hG1fDa8/5Ivw5a8QwyayZZB7u2EiZWGV2627LSllbyFFLJskAwymVKbwhlG7o/BjE/4+19rAa/2nZ409Z7yUXkb94m6g46Sy1pqqZyyYUXdxEuhRlI78q6eltyMY42keR+0/gw3sdUYTpny6XhyHSRp8szNdVLbA3nLl+bqscylUvW2kq/De+6UzMNJNBm7oIrBL1fIx1xFqM/GlouAl3OyY8d7L5rpjtlZfMRK5OMyETpf9vq90OFNVaGyaPubPpwSOrwyMdd++5IFXt6ib3TnAtWqt5188pkFNfjjG1AvyJr1ZOzzLmcyRrTkbpuYotiVI0YYJIkLN7BmEMvSeOJMk7bDQjwcgY9T8Vhl/Wb9S2G+ht4DKqG3nXxMv+z5F3/qITcuUXXMglxZbORylUX2eI6dNOyk8nVCkq+kzay7uKCGlp9s3Y2Pv9r3exvvsdBb+ddufcs0X6hSUxsGhAdYWDSN8vjSNeXDL2JO09ytR8TDpLlF+f3IKtZRPgxHcBavyijUtVP2aRlLZo5B5XzUyncH+w5NB2jzt62rHJ2C+Vp4T9Gvt6PJZKJMBbIB+Km64UDjbmpZ04E/zoVG+Cj82h+6MBiU0wx76QxgwOUnwcypIL05zMeGFiWNekN3wAfWz1mAC4/tfG3Uq33DuS12v1+fxrJBdYyc9WoG2Ck1Q7UDLXjrfWlz5wL4b7NY3nLeznvdurfUHhTH307aqqcIvQ+Og9/bqxf/b9Or3zhXH1qrV/eC+EPi6mt8t0416sWv7p7/787BH3ErAeOba/w/SoCrNgLeXOP/sJFwdN1Qv/j7O/zvNDL2D580EDC+eY3iFdl/3V1RwPjJnyhelYM/VhEwjr8fbHql95Sr65s71fuOEaOeo++3GpBFjE2v8H6zf3hTK2B8c4H/Acld7F+og3B8gxFjJQ6uFTEkxhpjZY7KMQQjRjPe5WMI1hiN2X+dxBCMGK3Yv7iJGTd/onjtOLi4vv4TawwEQRAEQRAEQRAEQRAEQRAEQRAEQRAEQRAEQRAEQRAEEfwf8TkB4ve++aEAAAAASUVORK5CYII=" height=150>
<img src="https://symbols.getvecta.com/stencil_86/44_kotlin.0ec1601067.png" height=200>
</p>

---

### What next?
Many features can be added in the future releases. The most important are
- room database to locally save posts
- add the possibility to comment the post
- allow user to see his following and followers

