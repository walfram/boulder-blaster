package test.boulder.prepared2;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Mesh.Mode;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

final class FaceMesh {

	private final List<Face> faces;

	public FaceMesh(List<Face> faces) {
		this.faces = faces;
	}

	public Mesh mesh() {
		Mesh mesh = new Mesh();
		mesh.setMode(Mode.Triangles);

		List<Vector3f> vertices = new ArrayList<>(faces.size() * 3);
		List<Vector3f> normals = new ArrayList<>(faces.size() * 3);
		List<Integer> indices = new ArrayList<>(faces.size() * 3);

		int idx = 0;
		for (Face face : faces) {
			vertices.add(face.v1());
			vertices.add(face.v2());
			vertices.add(face.v3());

			normals.add(face.normal());
			normals.add(face.normal());
			normals.add(face.normal());

			indices.add(idx++);
			indices.add(idx++);
			indices.add(idx++);
		}

		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices.stream().toArray(Vector3f[]::new)));
		mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices.stream().mapToInt(Integer::intValue).toArray()));

		mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals.stream().toArray(Vector3f[]::new)));

		mesh.updateCounts();
		mesh.updateBound();

		return mesh;
	}

}
