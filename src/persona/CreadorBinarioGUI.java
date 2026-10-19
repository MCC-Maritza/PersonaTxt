
package persona;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author maryse
 */
public class CreadorBinarioGUI extends JFrame {
    private JTextField txtNombre;
    private JTextField txtEdad;
    private JTextField txtCorreo;
    private JTextArea txtResultado;

    public CreadorBinarioGUI() {
        // Configuración de la ventana principal
        setTitle("Gestor de Archivos Binarios - Interfaz Gráfica");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder(" Datos de la Persona "));

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Edad:"));
        txtEdad = new JTextField();
        panelFormulario.add(txtEdad);

        panelFormulario.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelFormulario.add(txtCorreo);

        // Área de texto para visualizar datos leídos del binario
        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResultado);
        scrollPane.setBorder(BorderFactory.createTitledBorder(" Contenido del Archivo Binario "));

        // Botones de acción
        JButton btnGuardar = new JButton("Guardar en Binario");
        JButton btnLeer = new JButton("Leer Archivo Binario");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLeer);

        // Layout Principal
        setLayout(new BorderLayout(10, 10));
        add(panelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Margen exterior
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Eventos
        btnGuardar.addActionListener(e -> guardarEnArchivoBinario());
        btnLeer.addActionListener(e -> leerDesdeArchivoBinario());
    }

    private void guardarEnArchivoBinario() {
        String nombre = txtNombre.getText().trim();
        String edadStr = txtEdad.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isEmpty() || edadStr.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor completa todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número entero válido.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear instancia del objeto
        Persona persona = new Persona(nombre, edad, correo);

        // Selector de archivos para guardar
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como Archivo Binario");
        fileChooser.setSelectedFile(new File("datos.dat"));

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if (!archivo.getName().toLowerCase().endsWith(".dat") && !archivo.getName().toLowerCase().endsWith(".bin")) {
                archivo = new File(archivo.getAbsolutePath() + ".dat");
            }

            // Escribir el objeto en el archivo binario usando ObjectOutputStream
            try (FileOutputStream fos = new FileOutputStream(archivo);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                oos.writeObject(persona);

                JOptionPane.showMessageDialog(this, "¡Objeto guardado exitosamente en formato binario!\n" + archivo.getAbsolutePath());
                
                // Limpiar campos
                txtNombre.setText("");
                txtEdad.setText("");
                txtCorreo.setText("");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al escribir el archivo binario:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void leerDesdeArchivoBinario() {
        // Selector de archivos para abrir
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Archivo Binario");

        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();

            // Leer el objeto desde el archivo binario usando ObjectInputStream
            try (FileInputStream fis = new FileInputStream(archivo);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                // Deserializar objeto
                Persona persona = (Persona) ois.readObject();

                // Mostrar resultado en pantalla
                txtResultado.setText("Objeto recuperado del archivo binario:\n\n" + persona.toString());

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo binario:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "No se encontró la clase del objeto guardado.", "Error de clase", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}