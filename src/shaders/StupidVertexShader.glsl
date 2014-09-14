#version 150 core

in vec4 in_position;

out vec4 color;

void main(void){
	gl_Position = in_position;
	color = vec4(0,in_position.y,in_position.y,1.0);
}
