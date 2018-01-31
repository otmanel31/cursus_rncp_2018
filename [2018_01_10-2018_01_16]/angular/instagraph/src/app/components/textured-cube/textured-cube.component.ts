import { Component, OnInit, ElementRef, AfterViewInit, Input, ViewChild, OnChanges, SimpleChanges } from '@angular/core';

import * as THREE from 'three';
import { ImageRepositoryService } from '../../services/image-repository.service';

@Component({
  selector: 'app-textured-cube',
  templateUrl: './textured-cube.component.html',
  styleUrls: ['./textured-cube.component.css']
})
export class TexturedCubeComponent implements OnInit, AfterViewInit {

  // 2 types de camera, perspective (classique) ou isometrique sans perspective
  private camera: THREE.PerspectiveCamera;
  // la structure geoetrique (points, faces, etc) de notre cube
  private cube: THREE.Mesh;
  // la scene, le monde qui est rendu en 3d, avec camera, lumieres, objetx, etc...
  private scene: THREE.Scene;
  // moteur de rendu 3d (ici,, variante utilisant webgl, acceleration GPU)
  private renderer: THREE.WebGLRenderer;


  // canvas
  @ViewChild('canvas')
  private canvasRef : ElementRef;

  // getter en typescript
  public get canvas() : HTMLCanvasElement {
    // renvoie la balise html
    return this.canvasRef.nativeElement;
  }



  // les propriétés de mon cube
  @Input()
  public rotationSpeedX: number = 0.005;
  @Input()
  public rotationSpeedY: number = 0.01;
  @Input()
  public size: number = 200;
  @Input()
  public textureId: number = 2;

  // les propriété de la scene
  @Input()
  public cameraZ: number = 400;
  @Input()
  public fieldOfView: number = 70;
  @Input()
  public nearClippingPlane : number = 1;
  @Input()
  public farClippingPlane : number = 1000;

  constructor(private imageRepository: ImageRepositoryService) { }

  ngOnInit() {
  }

  // CREATION DU CUBE
  private createCube() : void {
    if (!this.textureId) {
      this.cube = null;
      return;
    }

    // chargement de l'image en texture
    let texture = new THREE.TextureLoader().load(this.imageRepository.getImageUrl(this.textureId));
    // material, texture + effet physique/visuel de rendu (metallique, reflet, etc) qui seront calculé
    let material = new THREE.MeshBasicMaterial( { map: texture});

    // structure (points/ faces) de notre objet cube
    let geometry = new THREE.BoxBufferGeometry(this.size, this.size, this.size);
    // on utilise ce squellette (geometrie) et on y applique un/des materiaux pour l'habiller
    // notre MESH est pret à l'emploie
    this.cube = new THREE.Mesh(geometry, material);

    // on le place dans la scene
    this.scene.add(this.cube);
  }

  // CREATION de la scene
  private createScene() : void {
    this.scene = new THREE.Scene();

    this.camera = new THREE.PerspectiveCamera(
                          this.fieldOfView,
                          this.getAspectRatio(),
                          this.nearClippingPlane,
                          this.farClippingPlane);
    //placement de la camera (devant le cube qui est  a 0.0.0)
    // si z augmente -> eloignement de la camera par rapport au cube
    this.camera.position.z = this.cameraZ;
  }

  // calcul de ratio horizontal/vertical
  private getAspectRatio() : number {
    return this.canvas.clientWidth / this.canvas.clientHeight;
  }

  public onResize() : void {
    // redimensionnement de la fenetre, donc
    // calcul du nouvel aspect ratio
    this.camera.aspect = this.getAspectRatio();
    // mise a jour de la projection 3D -> 2D en fonction
    this.camera.updateProjectionMatrix();
    // mise a jour de la taille de la fenetre de rendu
    this.renderer.setSize(this.canvas.clientWidth, this.canvas.clientHeight);
  }

  // animation du cube
  private animateCube() : void {
    if (this.cube != null) {
      this.cube.rotation.x += this.rotationSpeedX;
      this.cube.rotation.y += this.rotationSpeedY;
    }
  }

  // boucle qui se repete a chaque rafraichissement de l'image 3D
  private startRenderingLoop() : void {
    // creer le moteur de rendu
    this.renderer = new THREE.WebGLRenderer({ canvas: this.canvas});
    this.renderer.setPixelRatio(devicePixelRatio);
    this.renderer.setSize(this.canvas.clientWidth, this.canvas.clientHeight);
    // le moteur de rendu est pret
    let component: TexturedCubeComponent = this;

    // fonction rappelée a chaque rafraichissement
    (function render() {
      // je lui demande de rappeler ma fonction render quand il faudra
      // rafraichir la page
      requestAnimationFrame(render);
      // tourner le cube
      component.animateCube();
      // faire le rendu
      component.renderer.render(component.scene, component.camera);
    }());
  }
  // appelé une fois que le html et les inhections de contenu du parent
  // sont pret
  ngAfterViewInit(): void {
    this.createScene();
    this.createCube();
    this.startRenderingLoop();
  }


}
